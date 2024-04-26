package com.codeL.data.ds.atom;

import com.codeL.data.ds.common.model.lifecycle.Lifecycle;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.SQLException;

public interface AtomDsStandard extends DataSource, Lifecycle {

    void setLogWriter(PrintWriter out) throws SQLException;

    void setLoginTimeout(int seconds) throws SQLException;

    void destroyDataSource() throws Exception;

}
