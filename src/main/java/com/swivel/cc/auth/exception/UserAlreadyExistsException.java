package com.swivel.cc.auth.exception;

/**
 * This exception is  used in User service class.
 */
public class UserAlreadyExistsException extends AuthServiceException {

    /**
     * InvalidUser Exception with error message.
     *
     * @param errorMessage error message
     */
    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * InvalidUser Exception with error message.
     *
     * @param errorMessage error message
     */
    public UserAlreadyExistsException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

}
