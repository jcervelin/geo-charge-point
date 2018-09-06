package io.jcervelin.evchargingapi.domains.exceptions;

public class NoChargePointFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public NoChargePointFoundException(final String message) {
        super(message);
    }

}
