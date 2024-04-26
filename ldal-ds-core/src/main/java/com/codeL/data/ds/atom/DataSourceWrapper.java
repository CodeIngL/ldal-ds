package com.codeL.data.ds.atom;

import com.codeL.data.ds.common.exception.DsException;
import com.codeL.data.ds.common.model.lifecycle.Lifecycle;

import javax.sql.DataSource;

public interface DataSourceWrapper extends Lifecycle {

    DataSource getDataSource();

    void destroyDataSource() throws DsException;

}
