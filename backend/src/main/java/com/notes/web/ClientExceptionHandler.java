package com.notes.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.notes.utils.ExceptionUtil.getErrorMessageFromClientException;

@ControllerAdvice
class ClientExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public void handle(HttpServletResponse response, IllegalArgumentException e) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RestClientResponseException.class)
    public void handleClientRequests(HttpServletResponse response, RestClientResponseException e) throws IOException {
        String message = getErrorMessageFromClientException(e);
        response.sendError(e.getRawStatusCode(),message );
    }
}
