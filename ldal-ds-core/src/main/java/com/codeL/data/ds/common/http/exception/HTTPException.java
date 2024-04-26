package com.codeL.data.ds.common.http.exception;

public class HTTPException extends RuntimeException {

    private static final long serialVersionUID = -1680415265354142063L;

    private int httpCode = -1;

    public HTTPException() {
        super();
    }

    public HTTPException(int httpCode) {
        super();
        this.httpCode = httpCode;
    }

    public HTTPException(int httpCode, String message, Throwable cause) {
        super(message, cause);
        this.httpCode = httpCode;
    }

    public HTTPException(String message, Throwable cause) {
        super(message, cause);
    }

    public HTTPException(int httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }

    public HTTPException(Throwable cause) {
        super(cause);
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
}

