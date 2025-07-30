package com.jukisawa.discordBot.exceptions;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super("Data not found: " + message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super("Data not found: " + message, cause);
    }
}
