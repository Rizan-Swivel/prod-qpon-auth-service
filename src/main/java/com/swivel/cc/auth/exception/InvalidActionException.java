package com.swivel.cc.auth.exception;

/**
 * This exception is used to handle invalid actions.
 */
public class InvalidActionException extends AuthServiceException {
    /**
     * InvalidAction Exception with error message.
     *
     * @param errorMessage error message
     */
    public InvalidActionException(String errorMessage) {
        super(errorMessage);
    }

}
