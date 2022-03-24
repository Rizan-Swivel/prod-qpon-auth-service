package com.swivel.cc.auth.exception;

/**
 * Authentication Exception
 */
public class AuthServiceException extends RuntimeException {

    /**
     * Authentication Exception with error message.
     *
     * @param errorMessage error message
     */
    public AuthServiceException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Authentication Exception with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public AuthServiceException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
