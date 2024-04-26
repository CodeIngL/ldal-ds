
package com.codeL.data.ds;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.security.CodeSource;

/**
 * Version
 */
public final class Version {

    private static final Logger logger = LoggerFactory.getLogger(Version.class);

    private static final String VERSION = getVersion(Version.class, "");

    public static String getVersion() {
        return VERSION;
    }

    public static String getVersion(Class<?> cls, String defaultVersion) {
        try {
            Package pkg = cls.getPackage();
            String version = null;
            if (pkg != null) {
                version = pkg.getImplementationVersion();
                if (StringUtils.isNotEmpty(version)) {
                    return version;
                }

                version = pkg.getSpecificationVersion();
                if (StringUtils.isNotEmpty(version)) {
                    return version;
                }
            }

            CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                logger.info("No codeSource for class " + cls.getName() + " when getVersion, use default version " + defaultVersion);
                return defaultVersion;
            }

            URL location = codeSource.getLocation();
            if (location == null) {
                logger.info("No location for class " + cls.getName() + " when getVersion, use default version " + defaultVersion);
                return defaultVersion;
            }
            String file = location.getFile();
            if (!StringUtils.isEmpty(file) /*&& file.endsWith(".jar")*/) {
                return file;
                /*version = getFromFile(file);*/
            }

            return StringUtils.isEmpty(version) ? defaultVersion : version;
        } catch (Throwable e) {
            return defaultVersion;
        }
    }

    private static String getFromFile(String file) {

        file = file.substring(0, file.length() - 4);

        int i = file.lastIndexOf('/');
        if (i >= 0) {
            file = file.substring(i + 1);
        }

        i = file.indexOf("-");
        if (i >= 0) {
            file = file.substring(i + 1);
        }

        i = file.indexOf("-");
        if (i >= 0) {
            file = file.substring(i + 1);
        }

        while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
            i = file.indexOf("-");
            if (i >= 0) {
                file = file.substring(i + 1);
            } else {
                break;
            }
        }
        return file;
    }

}
