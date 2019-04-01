package com.stuto.core.pub;

/**
 * @author yongqiang.zhang
 * @version 1.0 , 2018/5/10 20:36
 */
public class StutoError extends Error {
    public StutoError(String message) {
        super(message);
    }

    public StutoError(String message, Throwable cause) {
        super(message, cause);
    }

    public static StutoError todo() {
        return new StutoError("Not Impl.");
    }

    public static StutoError unexpected() {
        return unexpected("Unexpected.");
    }

    public static StutoError unexpected(String message) {
        return new StutoError(message);
    }
}
