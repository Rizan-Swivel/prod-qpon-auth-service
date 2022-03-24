package com.swivel.cc.auth.exception;

/**
 * This exception is  used in User service class.
 */
public class ExpiredPasswordResetTokenException extends AuthServiceException {

    /**
     * InvalidPasswordResetToken Exception with error message.
     *
     * @param errorMessage error message
     */
    public ExpiredPasswordResetTokenException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * InvalidPasswordResetToken Exception with error message.
     *
     * @param errorMessage error message
     */
    public ExpiredPasswordResetTokenException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
