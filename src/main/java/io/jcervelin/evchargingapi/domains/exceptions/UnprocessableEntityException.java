package io.jcervelin.evchargingapi.domains.exceptions;

import lombok.Getter;

import java.util.Collection;

@Getter
public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException(final String message, Collection<String> errors) {
        super(message);
    }

    public UnprocessableEntityException(final String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;


    private Collection<String> errors;

}
