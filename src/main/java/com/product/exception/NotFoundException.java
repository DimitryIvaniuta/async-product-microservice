package com.product.exception;

/**
 * NotFoundException is a custom exception used to indicate that a requested resource was not found.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
