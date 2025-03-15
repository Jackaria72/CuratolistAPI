package com.artful.curatolist.controller.exception;

public class ExternalApiException  extends RuntimeException {
    public ExternalApiException(String message) {
        super(message);
    }
}
