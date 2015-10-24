package com.mateoj.hack2help.util;

/**
 * Created by jose on 10/23/15.
 */
public class Error {
    private String message;
    private int code;

    public Error(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
