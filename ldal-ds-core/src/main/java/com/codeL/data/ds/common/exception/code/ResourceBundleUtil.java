package com.codeL.data.ds.common.exception.code;


import com.codeL.data.ds.common.exception.DsRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * <pre>
 * ResourceBundle工具类
 * 不同的资源文件,创建不同的 ResourceBundleUtil,从该资源文件中,得到key对应的描述信息
 *
 * 使用方法:
 *
 * 资源文件 res/BundleName.properties内容如下:
 * key1=value1
 * key2=value2,{0}
 *
 * 代码:
 * ResourceBundleUtil util = new ResourceBundleUtil("res/BundleName");
 * util.getMessage("key1");                   //输出:value1
 * util.getMessage("key2","stone");           //输出:value2,stone
 * </pre>
 */
public class ResourceBundleUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResourceBundleUtil.class);
    private static final ResourceBundleUtil instance = new ResourceBundleUtil("res/ErrorCode");
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;
    public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;
    public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;

    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
    private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;
    private int systemPropertiesMode = 1;
    private boolean ignoreUnresolvablePlaceholders = false;
    private ResourceBundle bundle;                                                                             // 资源

    public static ResourceBundleUtil getInstance() {
        return instance;
    }


    public ResourceBundleUtil(String bundleName) {
        this.bundle = ResourceBundle.getBundle(bundleName);
    }


    public String getMessage(String key, int code, String type, String... params) {
        if (key == null) {
            return null;
        }
        String msg = bundle.getString(key);
        msg = parseStringValue(msg, bundle, new HashSet<String>());
        msg = StringUtils.replace(msg, "{code}", String.valueOf(code));
        msg = StringUtils.replace(msg, "{type}", String.valueOf(type));
        if (params == null || params.length == 0) {
            return msg;
        }
        if (StringUtils.isBlank(msg)) {
            return msg;
        }
        return MessageFormat.format(msg, params);
    }

    protected String parseStringValue(String strVal, ResourceBundle bundle, Set<String> visitedPlaceholders) {
        StringBuffer buf = new StringBuffer(strVal);
        int startIndex = strVal.indexOf(placeholderPrefix);
        while (startIndex != -1) {
            int endIndex = findPlaceholderEndIndex(buf, startIndex);
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + placeholderPrefix.length(), endIndex);
                if (!visitedPlaceholders.add(placeholder)) {
                    throw new DsRuntimeException(ErrorCode.ERR_CONFIG, "Circular placeholder reference '"
                            + placeholder + "' in bundle definitions");
                }

                placeholder = parseStringValue(placeholder, bundle, visitedPlaceholders);
                String propVal = resolvePlaceholder(placeholder, bundle, this.systemPropertiesMode);
                if (propVal != null) {
                    propVal = parseStringValue(propVal, bundle, visitedPlaceholders);
                    buf.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Resolved placeholder '" + placeholder + "'");
                    }
                    startIndex = buf.indexOf(this.placeholderPrefix, startIndex + propVal.length());
                } else if (this.ignoreUnresolvablePlaceholders) {
                    startIndex = buf.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
                } else {
                    throw new DsRuntimeException(ErrorCode.ERR_CONFIG, "Could not resolve placeholder '"
                            + placeholder + "'");
                }
                visitedPlaceholders.remove(placeholder);
            } else {
                startIndex = -1;
            }
        }

        return buf.toString();
    }

    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + placeholderPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (substringMatch(buf, index, placeholderSuffix)) {
                if (withinNestedPlaceholder > 0) {
                    withinNestedPlaceholder--;
                    index = index + placeholderSuffix.length();
                } else {
                    return index;
                }
            } else if (substringMatch(buf, index, placeholderPrefix)) {
                withinNestedPlaceholder++;
                index = index + placeholderPrefix.length();
            } else {
                index++;
            }
        }
        return -1;
    }

    private boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        for (int j = 0; j < substring.length(); j++) {
            int i = index + j;
            if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    private String resolvePlaceholder(String placeholder, ResourceBundle bundle, int systemPropertiesMode) {
        String propVal = null;
        if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE) {
            propVal = resolveSystemProperty(placeholder);
        }
        if (propVal == null) {
            propVal = resolvePlaceholder(placeholder, bundle);
        }
        if (propVal == null && systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK) {
            propVal = resolveSystemProperty(placeholder);
        }
        return propVal;
    }

    protected String resolvePlaceholder(String placeholder, ResourceBundle bundle) {
        return bundle.getString(placeholder);
    }

    private String resolveSystemProperty(String key) {
        try {
            String value = System.getProperty(key);
            if (value == null) {
                value = System.getenv(key);
            }
            return value;
        } catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not access system property '" + key + "': " + ex);
            }
            return null;
        }
    }

}
