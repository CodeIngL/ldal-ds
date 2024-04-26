package com.codeL.data.ds.atom.handle;

import com.codeL.data.ds.atom.config.DbPasswdManager;
import com.codeL.data.ds.atom.config.YamlSingleConfig;
import com.codeL.data.ds.atom.handle.conf.HandlerConf;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import com.google.common.eventbus.Subscribe;
import com.codeL.data.ds.common.AtomConstants;
import com.codeL.data.ds.common.exception.DsException;
import com.codeL.data.ds.common.exception.DsNestableRuntimeException;
import com.codeL.data.ds.common.model.lifecycle.Lifecycle;
import com.codeL.data.ds.event.DatasourceChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ApolloPullPushDsConfHandler extends AbstractDsConfHandle implements Lifecycle, DsConfHandle {

    private static final Logger logger = LoggerFactory.getLogger(ApolloPullPushDsConfHandler.class);

    private static final String NAMESPACE_PREFIX = "middleware-kunu-";

    private static final String EAGER = "eager";

    private static final String RETRY_COUNT = "retryCount";

    private final ReentrantLock lock = new ReentrantLock();

    private final String subKey;

    private DbPasswdManager dbPasswdManager;

    private volatile DataSource dataSource = null;

    public ApolloPullPushDsConfHandler(String appName, String dbKey, String unitName, HandlerConf conf) {
        super(appName, dbKey, unitName, conf);
        subKey = AtomConstants.cacheKey(this.getUnitName(), this.getAppName(), this.getDbKey());
    }

    @Override
    protected void doInit() throws DsException {
        super.doInit();
        //try to init paswwManager
        ConfigFile configFile = ConfigService.getConfigFile(NAMESPACE_PREFIX + getDbKey(), ConfigFileFormat.YML);
        String content = configFile.getContent();
        _dealSubscribeEvent(new ConfigFileChangeEvent(NAMESPACE_PREFIX + getDbKey(), null, content, PropertyChangeType.ADDED), true);
        configFile.addChangeListener(this::dealSubscribeEvent);
    }

    private void postDatasourceEvent(DatasourceChangeEvent datasourceChangeEvent) {
        eventBus.post(datasourceChangeEvent);
    }

    @Override
    public DataSource getDataSource() {
        if (dataSource == null) {
            throw new DsNestableRuntimeException("current datasource is null. please check datasource config");
        }
        return dataSource;
    }

    @Subscribe
    public final void renew(final DatasourceChangeEvent datasourceChangeEvent) {
        if (datasourceChangeEvent == null) {
            return;
        }
        if (datasourceChangeEvent.getOldDatasource() == null) {
            return;
        }
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                //ignore
            }
            DataSource dataSource = datasourceChangeEvent.getOldDatasource();
            if (dataSource instanceof Closeable) {
                String key = datasourceChangeEvent.getKey();
                logger.info("old [DataSource:{} Stop] Start!", key);
                try {
                    ((Closeable) dataSource).close();
                } catch (IOException e) {
                    logger.warn("close oldDatasource:{} failed!", key, e);
                }
                logger.info("old [DataSource:{} Stop] End!", key);
            }
        }).start();

    }


    private void dealSubscribeEvent(ConfigFileChangeEvent fileChangeEvent) {
        _dealSubscribeEvent(fileChangeEvent, false);
    }

    private void _dealSubscribeEvent(ConfigFileChangeEvent fileChangeEvent, boolean direct) {
        String appConfStr = fileChangeEvent.getNewValue();
        if (appConfStr == null || "".equals(appConfStr)) {
            logger.info("defend guard for refreshing datasource:{}. newValue is null", subKey);
            return;
        }
        logger.info("refreshing datasource:{}. newValue is {}", subKey, appConfStr);
        lock.lock();
        try {
            YamlSingleConfig config = YamlSingleConfig.unmarshal(appConfStr.getBytes());
            DataSource preDataSource = this.dataSource;
            DataSource curDataSource = config.getDataSource();
          /*  if (curDataSource != null && DruidDataSource.class.isAssignableFrom(curDataSource.getClass())) {
                ((DruidDataSource) curDataSource).setPassword(((DruidDataSource) curDataSource).getPassword());
            }*/
            Map<String, String> propConf = config.getProps();
            boolean eager = false;
            int count = 3;
            if (propConf != null && propConf.size() != 0) {
                if (propConf.containsKey(EAGER)) {
                    eager = "TRUE".equalsIgnoreCase(propConf.get(EAGER));
                }
                String countStr = propConf.get(RETRY_COUNT);
                if (countStr != null && !"".equals(countStr)) {
                    count = Integer.parseInt(countStr);
                }
            }
            if (eager) {
                int retryCount = 0;
                boolean hasException = true;
                do {
                    try {
                        Connection connection = curDataSource.getConnection();
                        connection.close();
                        hasException = false;
                        break;
                    } catch (Throwable throwable) {
                        if (retryCount == 0) {
                            logger.error("try to refresh datasource:{}. retryCount {}, " +
                                    "but course error for new datasource, apply to with guard! newValue is {}", retryCount, subKey, appConfStr, throwable);
                        } else {
                            logger.error("try to refresh datasource:{}. retryCount {}, " +
                                    "but course error for new datasource, apply to with guard! newValue is {}", retryCount, subKey, appConfStr);
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        //ignore
                    }
                    retryCount++;
                }
                while (--count > 0);
                if (hasException) {
                    if (curDataSource instanceof Closeable) {
                        ((Closeable) curDataSource).close();
                    }
                    return;
                }
            }
            this.dataSource = curDataSource;
            postDatasourceEvent(new DatasourceChangeEvent(preDataSource, curDataSource, subKey));
        } catch (Throwable throwable) {
            logger.error("refresh datasource:{} failed. newValue is {}", subKey, appConfStr);
            throw new DsNestableRuntimeException(throwable);
        } finally {
            lock.unlock();
        }
        logger.info("refreshed datasource:{} success .", subKey);
    }

    @Override
    protected void doDestroy() throws DsException {
        DataSource dataSource = this.dataSource;
        if (dataSource == null) {
            return;
        }
        if (dataSource instanceof Closeable) {
            logger.info("[DataSource Stop] Start!");
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                logger.warn("DataSource close failed!", e);
            }
            logger.info("[DataSource Stop] End!");
        }
    }
}
