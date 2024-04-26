package com.codeL.data.ds.atom.handle;

import com.codeL.data.ds.atom.handle.conf.HandlerConf;
import com.codeL.data.ds.common.model.lifecycle.AbstractLifecycle;
import com.codeL.data.ds.common.exception.DsException;
import com.codeL.data.ds.common.exception.code.ErrorCode;
import com.codeL.data.ds.common.model.lifecycle.Lifecycle;
import org.apache.commons.lang.StringUtils;

public abstract class AbstractDsConfHandle extends AbstractLifecycle implements Lifecycle, DsConfHandle {

    private final String appName;

    private final String dbKey;

    private final String unitName;

    private HandlerConf conf;

    public AbstractDsConfHandle(String appName, String dbKey, String unitName, HandlerConf conf) {
        this.appName = appName;
        this.dbKey = dbKey;
        this.unitName = unitName;
        this.conf = conf;
    }

    public String getAppName() {
        return appName;
    }

    public String getDbKey() {
        return dbKey;
    }

    public String getUnitName() {
        return unitName;
    }

    public HandlerConf getConf() {
        return conf;
    }

    public void setConf(HandlerConf conf) {
        this.conf = conf;
    }

    @Override
    protected void doInit() throws DsException {
        if (StringUtils.isBlank(getAppName()) || StringUtils.isBlank(getDbKey())) {
            throw new DsException(ErrorCode.ERR_CONFIG_MISS_ATOM_CONFIG, getDbKey(), "appName or dbKey");
        }
        register();
    }

    @Override
    public void destroyDataSource() throws DsException {
        destroy();
    }
}
