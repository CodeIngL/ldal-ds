package com.codeL.data.ds.common.model.lifecycle;

import com.codeL.data.ds.common.exception.DsException;

public class AbstractLifecycle implements Lifecycle {

    protected final Object lock = new Object();
    protected volatile boolean isInited = false;

    public void init() throws DsException {
        synchronized (lock) {
            if (isInited()) {
                return;
            }

            try {
                doInit();
                isInited = true;
            } catch (Exception e) {
                // 出现异常调用destory方法，释放
                try {
                    doDestroy();
                } catch (Exception e1) {
                    // ignore
                }
                throw new DsException(e);
            }
        }
    }

    public void destroy() throws DsException {
        synchronized (lock) {
            if (!isInited()) {
                return;
            }
            doDestroy();
            isInited = false;
        }
    }

    public boolean isInited() {
        return isInited;
    }

    protected void doInit() throws DsException {
    }

    protected void doDestroy() throws DsException {
    }

}
