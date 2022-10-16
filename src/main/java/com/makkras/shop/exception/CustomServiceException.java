package com.makkras.shop.exception;

public class CustomServiceException extends Exception{
    public CustomServiceException() {
    }

    public CustomServiceException(String message) {
        super(message);
    }

    public CustomServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomServiceException(Throwable cause) {
        super(cause);
    }
}
