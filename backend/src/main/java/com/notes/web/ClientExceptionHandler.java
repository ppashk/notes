package com.notes.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.notes.utils.ExceptionUtil.getErrorMessageFromClientException;

@ControllerAdvice
class ClientExceptionHandler {

    @ExceptionHandler(RestClientResponseException.class)
    public void handleClientRequests(HttpServletResponse response, RestClientResponseException e) throws IOException {
        response.sendError(e.getRawStatusCode(), getErrorMessageFromClientException(e));
    }
}
