package io.jcervelin.evchargingapi.domains.exceptions;

public class WrongChargingPointFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public WrongChargingPointFoundException(final String message) {
        super(message);
    }

}
