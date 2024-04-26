package com.codeL.data.ds.common.exception.code;

public enum ErrorCode {

    ERR_CONFIG(ErrorType.Config, 4000),

    ERR_CONFIG_MISS_ATOM_CONFIG(ErrorType.Config, 4001);

    ErrorCode(ErrorType type, int code) {
        this.code = code;
        this.type = type;
    }

    private int code;
    private ErrorType type;

    public int getCode() {
        return code;
    }

    public String getType() {
        return type.name();
    }

    public String getMessage(String... params) {
        return ResourceBundleUtil.getInstance().getMessage(this.name(), this.getCode(),
                this.getType(), params);
    }
}

enum ErrorType {
    Config
}
