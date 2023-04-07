package com.notes.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import static com.notes.utils.ExceptionUtil.getErrorMessageFromClientException;

@ControllerAdvice
class ClientExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<String> handleClientRequests(RestClientResponseException e) {
        return ResponseEntity.status(e.getRawStatusCode()).body(getErrorMessageFromClientException(e));
    }
}
