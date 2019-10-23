package org.wayne.java;

public class WrappedRuntimeException extends RuntimeException {
    private Throwable t;
    public WrappedRuntimeException(Throwable t) {
        this.t = t;
    }
    public Throwable throwable() {
        return t;
    }
}
