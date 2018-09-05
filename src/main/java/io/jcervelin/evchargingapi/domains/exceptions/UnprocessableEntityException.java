package io.jcervelin.evchargingapi.domains.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class UnprocessableEntityException extends RuntimeException {

    private String message;
    private Collection<String> errors;

    public UnprocessableEntityException(final String message) {
        super(message);
        this.message = message;
    }

    private static final long serialVersionUID = 1L;

}
