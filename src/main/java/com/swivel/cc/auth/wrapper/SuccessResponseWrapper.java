package com.swivel.cc.auth.wrapper;

import com.swivel.cc.auth.enums.ResponseStatusType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.domain.response.ResponseDto;
import lombok.Getter;

/**
 * Success Response Wrapper
 */
@Getter
public class SuccessResponseWrapper extends ResponseWrapper {

    private final String displayMessage;

    public SuccessResponseWrapper(SuccessResponseStatusType successResponseStatusType, String message,
                                  ResponseDto data) {
        super(ResponseStatusType.SUCCESS, successResponseStatusType.getMessage(), data);
        this.displayMessage = message;
    }
}
