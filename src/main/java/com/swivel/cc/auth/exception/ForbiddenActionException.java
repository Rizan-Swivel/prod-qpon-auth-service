package com.swivel.cc.auth.exception;

/**
 * This exception used in admin service class
 */
public class ForbiddenActionException extends AuthServiceException {

    /**
     * ForbiddenActionException Exception with error message.
     *
     * @param errorMessage error message
     */
    public ForbiddenActionException(String errorMessage) {
        super(errorMessage);
    }

}
