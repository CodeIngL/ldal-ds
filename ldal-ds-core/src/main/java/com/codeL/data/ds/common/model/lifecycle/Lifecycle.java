package com.codeL.data.ds.common.model.lifecycle;

import com.codeL.data.ds.common.exception.DsException;

public interface Lifecycle {

    void init() throws DsException;

    void destroy() throws DsException;

    boolean isInited() throws DsException;

}
