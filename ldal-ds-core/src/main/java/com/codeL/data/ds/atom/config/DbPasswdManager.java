package com.codeL.data.ds.atom.config;

import com.codeL.data.ds.common.exception.DsException;

public interface DbPasswdManager {

    String getPasswd();

    void stopDbPasswdManager() throws DsException;
}
