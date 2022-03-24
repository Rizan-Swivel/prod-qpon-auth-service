package com.swivel.cc.auth.exception;

/**
 * This exception is  used in Admin service class.
 */
public class InvalidPermissionException extends AuthServiceException {

    /**
     * InvalidPermissionException Exception with error message.
     *
     * @param errorMessage error message
     */
    public InvalidPermissionException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * InvalidPermissionException Exception with error message.
     *
     * @param errorMessage error message
     */
    public InvalidPermissionException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

}
