package com.codeL.data.ds.common;

import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;

public class AtomConstants {

    /**
     * dbName
     */
    private static final MessageFormat DB_NAME_FORMAT = new MessageFormat("atom.dbkey.{0}^{1}^{2}");

    /**
     * default
     */
    private static final String NULL_UNIT_NAME = "DEFAULT_UNIT";

    /**
     * @param unitName
     * @param appName
     * @param dbkey
     * @return
     */
    public static String cacheKey(String unitName, String appName, String dbkey) {
        if (StringUtils.isEmpty(unitName)) {
            return DB_NAME_FORMAT.format(new Object[]{NULL_UNIT_NAME, appName, dbkey});
        } else {
            return DB_NAME_FORMAT.format(new Object[]{unitName, appName, dbkey});
        }
    }
}
