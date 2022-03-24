package com.swivel.cc.auth.exception;

/**
 * EmailAlreadyExists Exception
 */
public class NoEmailAccessException extends AuthServiceException {

    /**
     * EmailAlreadyExists Exception with error message
     *
     * @param errorMessage errorMessage
     */
    public NoEmailAccessException(String errorMessage) {
        super(errorMessage);
    }
}
