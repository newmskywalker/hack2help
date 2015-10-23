package com.mateoj.hack2help.util;

/**
 * Created by jose on 10/23/15.
 */
public interface Callback<T> {
    void done(T result);
    void error(Error error);
}
