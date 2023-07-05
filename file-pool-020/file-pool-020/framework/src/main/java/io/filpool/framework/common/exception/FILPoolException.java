package io.filpool.framework.common.exception;

/**
 * 全局异常处理类
 */
public class FILPoolException extends Exception {
    private String msgKey;

    public FILPoolException(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgKey() {
        return msgKey;
    }
}
