package com.codeL.data.ds.common.exception;

import com.codeL.data.ds.common.exception.code.ErrorCode;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.sql.SQLException;


public class DsException extends SQLException {

    private static final long serialVersionUID = 1540164086674285095L;

    public DsException(ErrorCode errorCode, String... params){
        super(errorCode.getMessage(params), "ERROR", errorCode.getCode());
    }

    public DsException(ErrorCode errorCode, Throwable cause, String... params){
        super(errorCode.getMessage(params), "ERROR", errorCode.getCode(), cause);
    }

    public DsException(Throwable cause){
        super(cause.getMessage(), cause);
    }

    public String toString() {
        return getLocalizedMessage();
    }


    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        } else {
            Throwable ca = this;
            do {
                Throwable c = ExceptionUtils.getCause(ca);
                if (c != null) {
                    ca = c;
                } else {
                    break;
                }
            } while (ca.getMessage() == null);
            return ca.getMessage();
        }
    }
}
