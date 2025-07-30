package com.jukisawa.discordBot.exceptions;

import java.io.IOException;

public class WrappedIOException extends RuntimeException {
    private final IOException cause;

    public WrappedIOException(IOException cause) {
        super(cause);
        this.cause = cause;
    }

    public IOException unwrap() {
        return cause;
    }

    @Override
    public synchronized IOException getCause() {
        return cause;
    }
}
