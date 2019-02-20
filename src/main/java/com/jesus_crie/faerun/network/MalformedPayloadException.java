package com.jesus_crie.faerun.network;

/**
 * Indicate that the data that was read as a payload isn't one.
 */
public class MalformedPayloadException extends RuntimeException {

    public MalformedPayloadException() {
        super();
    }

    public MalformedPayloadException(final String message) {
        super(message);
    }

    public MalformedPayloadException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
