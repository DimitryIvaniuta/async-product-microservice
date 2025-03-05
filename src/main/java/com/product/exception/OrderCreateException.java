package com.product.exception;

/**
 * OrderCreateException is a custom exception used to indicate that Item Order cannot be created.
 */
public class OrderCreateException extends RuntimeException {

    public OrderCreateException() {
        super();
    }

    public OrderCreateException(String message) {
        super(message);
    }

    public OrderCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderCreateException(Throwable cause) {
        super(cause);
    }

}
