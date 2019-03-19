package com.jesus_crie.faerun.network;

/**
 * Indicate that the data that was read as a payload isn't one.
 */
public class MalformedPayloadException extends RuntimeException {

    private static final long serialVersionUID = -5391668410201814495L;

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
