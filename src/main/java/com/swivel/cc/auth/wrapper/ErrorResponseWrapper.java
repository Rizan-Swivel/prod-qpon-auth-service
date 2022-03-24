package com.swivel.cc.auth.wrapper;

import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.enums.ResponseStatusType;
import com.swivel.cc.auth.domain.response.ResponseDto;
import lombok.Getter;

/**
 * ErrorResponseWrapper
 */
@Getter
public class ErrorResponseWrapper extends ResponseWrapper {

    private final int errorCode;
    private final String displayMessage;

    public ErrorResponseWrapper(ErrorResponseStatusType errorResponseStatusType, String message, ResponseDto data) {
        super(ResponseStatusType.ERROR, errorResponseStatusType.getMessage(), data);
        this.errorCode = errorResponseStatusType.getCode();
        this.displayMessage = message;
    }

}
