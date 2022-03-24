package com.swivel.cc.auth.exception;

/**
 * InvalidPassword Exception
 */
public class InvalidPasswordException extends AuthServiceException {

    /**
     * InvalidPassword Exception with error message
     *
     * @param errorMessage errorMessage
     */
    public InvalidPasswordException(String errorMessage) {
        super(errorMessage);
    }

}
