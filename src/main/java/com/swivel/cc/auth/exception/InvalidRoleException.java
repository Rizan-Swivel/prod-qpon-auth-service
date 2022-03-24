package com.swivel.cc.auth.exception;

/**
 * This exception is  used in Admin service class.
 */
public class InvalidRoleException extends AuthServiceException {

    /**
     * InvalidRoleException Exception with error message.
     *
     * @param errorMessage error message
     */
    public InvalidRoleException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * InvalidRoleException Exception with error message.
     *
     * @param errorMessage error message
     */
    public InvalidRoleException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

}
