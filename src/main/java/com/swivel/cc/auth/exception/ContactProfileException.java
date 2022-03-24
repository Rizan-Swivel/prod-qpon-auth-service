package com.swivel.cc.auth.exception;

/**
 * This exception is  used to handle merchant contact profile related exceptions.
 */
public class ContactProfileException extends AuthServiceException {
    /**
     * Authentication Exception with error message.
     *
     * @param errorMessage error message
     */
    public ContactProfileException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Authentication Exception with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public ContactProfileException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
