package io.jcervelin.evchargingapi.domains.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private List<String> errors;

    public void addError(final String error) {
        errors = Optional.ofNullable(errors).orElse(new ArrayList<>());
        errors.add(error);
    }

}