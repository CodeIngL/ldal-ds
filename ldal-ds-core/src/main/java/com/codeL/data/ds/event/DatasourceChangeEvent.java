package com.codeL.data.ds.event;

import javax.sql.DataSource;

public class DatasourceChangeEvent {

    private DataSource oldDatasource;

    private DataSource newDatasource;

    private String key;

    public DatasourceChangeEvent(DataSource oldDatasource, DataSource newDatasource, String key) {
        this.oldDatasource = oldDatasource;
        this.newDatasource = newDatasource;
        this.key = key;
    }

    public DataSource getOldDatasource() {
        return oldDatasource;
    }

    public DataSource getNewDatasource() {
        return newDatasource;
    }

    public String getKey() {
        return key;
    }
}
