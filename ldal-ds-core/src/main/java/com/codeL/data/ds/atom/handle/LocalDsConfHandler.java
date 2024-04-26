package com.codeL.data.ds.atom.handle;

import com.codeL.data.ds.atom.config.YamlSingleConfig;
import com.codeL.data.ds.atom.file.FileUtils;
import com.codeL.data.ds.atom.file.YamlWatchdog;
import com.codeL.data.ds.atom.handle.conf.HandlerConf;
import com.codeL.data.ds.common.AtomConstants;
import com.codeL.data.ds.common.model.lifecycle.Lifecycle;
import com.codeL.data.ds.common.exception.DsNestableRuntimeException;
import com.codeL.data.ds.common.exception.DsException;
import com.codeL.data.ds.event.DatasourceChangeEvent;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class LocalDsConfHandler extends AbstractDsConfHandle implements Lifecycle, DsConfHandle {

    private static final Logger logger = LoggerFactory.getLogger(LocalDsConfHandler.class);

    private static final String META_SUFFIX = ".meta";

    private static final String EAGER = "eager";

    private volatile DataSource dataSource;

    private final String subKey;

    public LocalDsConfHandler(String appName, String dbKey, String unitName, HandlerConf conf) {
        super(appName, dbKey, unitName, conf);
        subKey = AtomConstants.cacheKey(this.getUnitName(), this.getAppName(), this.getDbKey());

    }

    @Override
    protected void doInit() throws DsException {
        super.doInit();
        String path = buildDataFile();
        buildMetaFile(path);
        YamlWatchdog watchdog = new YamlWatchdog(path, this::rebuild);
        watchdog.setDelay(20000L);
        watchdog.start();
    }

    private void buildMetaFile(String path) throws DsException {
        File file;
        String metaPath = path + META_SUFFIX;
        file = new File(metaPath);
        if (!file.exists()) {
            logger.info("db.meta config file not exists! auto touch it");
            try {
                FileUtils.touch(file);
            } catch (IOException e) {
                logger.warn("db.meta config file not exists!  touch it failed", e);
                throw new DsException(e);
            }
        }
    }

    private String buildDataFile() throws DsException {
        String path = System.getProperty("user.home") + File.separator + getUnitName() + File.separator + getAppName() + File.separator + getDbKey();
        File file = new File(path);
        if (!file.exists()) {
            logger.info("db config file not exists! auto touch it");
            try {
                FileUtils.touch(file);
            } catch (IOException e) {
                logger.warn("db config file not exists!  touch it failed", e);
                throw new DsException(e);
            }
        }
        return path;
    }

    public void rebuild(String fileName, String content) {
        try {
            YamlSingleConfig config = YamlSingleConfig.unmarshal(content.getBytes());;
            DataSource preDataSource = this.dataSource;
            DataSource curDataSource = config.getDataSource();
            Map<String, String> propConf = config.getProps();
            boolean eager = true;
            if (propConf != null && propConf.size() != 0) {
                eager = "TRUE".equalsIgnoreCase(propConf.get(EAGER));
            }
            if (eager) {
                Connection connection = curDataSource.getConnection();
                connection.close();
            }
            this.dataSource = curDataSource;
            postDatasourceEvent(new DatasourceChangeEvent(preDataSource, curDataSource, subKey));
        } catch (SQLException | IOException e) {
            logger.error("refresh datasource:{} failed. newValue is {}", fileName, content);
            throw new DsNestableRuntimeException(e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    protected void doDestroy() throws DsException {
        DataSource dataSource = this.dataSource;
        if (dataSource instanceof Closeable) {
            logger.info("[DataSource Stop] Start!");
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("[DataSource Stop] End!");
        }
    }

    private void postDatasourceEvent(DatasourceChangeEvent datasourceChangeEvent) {
        eventBus.post(datasourceChangeEvent);
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

}
