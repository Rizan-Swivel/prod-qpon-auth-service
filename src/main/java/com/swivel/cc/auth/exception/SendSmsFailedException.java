package com.swivel.cc.auth.exception;

/**
 * SendSmsFailedException
 */
public class SendSmsFailedException extends AuthServiceException {

    /**
     * SendSmsFailedException Exception with error message.
     *
     * @param errorMessage error message
     */
    public SendSmsFailedException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * SendSmsFailedException Exception with error message.
     *
     * @param errorMessage error message
     */
    public SendSmsFailedException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

}
