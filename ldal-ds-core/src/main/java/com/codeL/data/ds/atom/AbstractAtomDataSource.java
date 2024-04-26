package com.codeL.data.ds.atom;

import com.codeL.data.ds.common.model.lifecycle.AbstractLifecycle;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractAtomDataSource extends AbstractLifecycle implements AtomDsStandard {

    protected abstract DataSource getDataSource() throws SQLException;

    public abstract void destroyDataSource() throws Exception;

    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return getDataSource().getConnection(username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return getDataSource().getLogWriter();
    }

    public int getLoginTimeout() throws SQLException {
        return getDataSource().getLoginTimeout();
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        getDataSource().setLogWriter(out);

    }

    public void setLoginTimeout(int seconds) throws SQLException {
        getDataSource().setLoginTimeout(seconds);
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        } else {
            throw new SQLException("not a wrapper for " + iface);
        }
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return AbstractAtomDataSource.class.isAssignableFrom(iface);
    }

}
