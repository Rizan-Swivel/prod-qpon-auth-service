package com.swivel.cc.auth.exception;

/**
 * This exception is  used to handle merchant business profile related exceptions.
 */
public class BusinessProfileException extends AuthServiceException {
    /**
     * Authentication Exception with error message.
     *
     * @param errorMessage error message
     */
    public BusinessProfileException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Authentication Exception with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public BusinessProfileException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
