package io.jcervelin.evchargingapi.gateways.http.advice;

import io.jcervelin.evchargingapi.domains.api.ErrorResponse;
import io.jcervelin.evchargingapi.domains.exceptions.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionControllerAdvice {

    private static final String ERROR_NOT_DEFINITION = "Unidentified error";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        return createMessage(exception, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponse> noContentFound(final UnprocessableEntityException exception) {
        return createMessage(exception, UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<ErrorResponse> createMessage(final Exception exception, final HttpStatus httpStatus) {
        log.info("handleException", exception);
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(httpStatus);
        errorResponse.addError(exception.getMessage().isEmpty() ? ERROR_NOT_DEFINITION : exception.getMessage());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    private ResponseEntity<ErrorResponse> createMessage(final UnprocessableEntityException exception, final HttpStatus httpStatus) {
        log.info("handleException", exception);
        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(httpStatus);
        exception.getErrors().forEach(errorResponse::addError);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

}