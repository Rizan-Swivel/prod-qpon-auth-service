package com.swivel.cc.auth.exception;

public class InvalidDateOptionException extends AuthServiceException{
    /**
     * Authentication Exception with error message.
     *
     * @param errorMessage error message
     */
    public InvalidDateOptionException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Authentication Exception with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public InvalidDateOptionException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
