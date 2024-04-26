package com.codeL.data.ds.atom;

import com.codeL.data.ds.atom.handle.DsConfHandle;
import com.codeL.data.ds.atom.handle.conf.HandlerConf;
import com.codeL.data.ds.atom.handle.loader.DsConfHandlerLoader;
import com.codeL.data.ds.common.exception.DsException;
import com.codeL.data.ds.common.AtomConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Closeable;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;

public final class AtomDataSource extends AbstractAtomDataSource implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(AtomDataSource.class);

    private static Map<String, DsConfHandle> cacheConfHandleMap = new HashMap<>();

    private static Map<String, Class<?>> dsConfHandleClasses = new HashMap<>();

    static {
        DsConfHandlerLoader.LOADER
                .load(dsConfHandleClasses, "META-INF/kunu/services/", DsConfHandle.class.getTypeName());
    }

    private String appName;

    private String dbKey;

    private String unitName = "default";

    private String code;

    private String server;

    private String handlerType = "apolloPullPush";

    private HandlerConf conf = null;

    private volatile DsConfHandle confHandle;

    @Override
    public void doInit() throws DsException {
        String cacheKey = AtomConstants.cacheKey(this.getUnitName(), this.getAppName(), this.getDbKey());
        synchronized (cacheConfHandleMap) {
            DsConfHandle cacheConfHandle = cacheConfHandleMap.get(cacheKey);
            if (null == cacheConfHandle) {
                if (conf == null) {
                    conf = new HandlerConf();
                }
                conf.put("code", code);
                conf.put("server", server);
                Class cls = dsConfHandleClasses.get(handlerType);
                if (cls == null) {
                    logger.error("DsConfHandle for handlerType:{} not found, dsCacheKey:{}", handlerType, cacheKey);
                    throw new IllegalStateException("DsConfHandle for handlerType:" + handlerType +
                            " not found, dsCacheKey:" + cacheKey);
                }
                try {
                    Constructor constructor = cls.getDeclaredConstructor(String.class, String.class, String.class, HandlerConf.class);
                    confHandle = (DsConfHandle) constructor.newInstance(appName, dbKey, unitName, conf);
                } catch (Throwable throwable) {
                    logger.error("constructor DsConfHandle failed! handlerType:{}, dsCacheKey:{}", handlerType, cacheKey, throwable);
                    throw new IllegalStateException("constructor DsConfHandle failed! handlerType:" + handlerType + ", dsCacheKey:" + cacheKey);
                }
                this.confHandle.init();
                cacheConfHandleMap.put(cacheKey, confHandle);
                logger.info("create new DsConfHandle key : " + cacheKey);
            } else {
                confHandle = cacheConfHandle;
                logger.info("use the cache DsConfHandle key : " + cacheKey);
            }
        }
    }

    public static void cleanAllDataSource() {
        synchronized (cacheConfHandleMap) {
            for (DsConfHandle handles : cacheConfHandleMap.values()) {
                try {
                    handles.destroyDataSource();
                } catch (Exception e) {
                    logger.warn("destory DsConfHandle failed!", e);
                    continue;
                }
            }
            cacheConfHandleMap.clear();
        }
    }

    @Override
    protected void doDestroy() throws DsException {
        String dbName = AtomConstants.cacheKey(this.getUnitName(), this.getAppName(), this.getDbKey());
        synchronized (cacheConfHandleMap) {
            this.confHandle.destroyDataSource();
            cacheConfHandleMap.remove(dbName);
        }
    }

    @Override
    public void destroyDataSource() throws Exception {
        destroy();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public HandlerConf getConf() {
        return conf;
    }

    public void setConf(HandlerConf conf) {
        this.conf = conf;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    @Override
    public DataSource getDataSource() throws SQLException {
        if (!isInited) {
            init();
        }
        return this.confHandle.getDataSource();
    }

    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public void close() {
        try {
            destroy();
        } catch (DsException e) {
            logger.warn("close failed!", e);
        }
    }
}
