package com.codeL.data.ds.common.exception;

import com.codeL.data.ds.common.exception.code.ErrorCode;

public class DsRuntimeException extends DsNestableRuntimeException {

    private static final long serialVersionUID = -654893533794556357L;

    private int vendorCode = 0;

    public DsRuntimeException(ErrorCode errorCode, String... params) {
        super(errorCode.getMessage(params));
        this.vendorCode = errorCode.getCode();
    }

    public DsRuntimeException(ErrorCode errorCode, Throwable cause, String... params) {
        super(errorCode.getMessage(params), cause);
        this.vendorCode = errorCode.getCode();
    }

    public String toString() {
        return super.getLocalizedMessage();
    }

    @Override
    public String getMessage() {
        if (vendorCode > 0) {
            return super.getMessage();
        } else {
            return super.getMessage();
        }
    }
}
