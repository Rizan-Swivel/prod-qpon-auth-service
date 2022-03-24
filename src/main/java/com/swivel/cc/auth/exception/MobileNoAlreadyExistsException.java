package com.swivel.cc.auth.exception;

/**
 * MobileNoAlreadyExists Exception
 */
public class MobileNoAlreadyExistsException extends AuthServiceException {

    /**
     * MobileNoAlready Exists Exception with error message
     *
     * @param errorMessage errorMessage
     */
    public MobileNoAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
