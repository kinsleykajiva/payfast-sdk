package com.payfast.exceptions;

public class InvalidRequestException extends Exception {
    public InvalidRequestException(String message, int code) {
        super(message);
    }
}
