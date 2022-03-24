package com.swivel.cc.auth.wrapper;

import com.swivel.cc.auth.domain.BaseDto;
import com.swivel.cc.auth.enums.ResponseStatusType;
import com.swivel.cc.auth.domain.response.ResponseDto;
import lombok.Getter;

/**
 * ResponseWrapper
 */
@Getter
public class ResponseWrapper implements BaseDto {

    private ResponseStatusType status;
    private String message;
    private ResponseDto data;

    public ResponseWrapper(ResponseStatusType statusType, String message, ResponseDto data) {
        this.status = statusType;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}

