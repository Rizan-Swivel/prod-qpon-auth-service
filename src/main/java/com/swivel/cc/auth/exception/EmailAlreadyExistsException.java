package com.swivel.cc.auth.exception;

/**
 * EmailAlreadyExists Exception
 */
public class EmailAlreadyExistsException extends AuthServiceException {

    /**
     * EmailAlreadyExists Exception with error message
     *
     * @param errorMessage errorMessage
     */
    public EmailAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
