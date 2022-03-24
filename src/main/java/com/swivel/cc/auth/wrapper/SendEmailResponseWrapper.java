package com.swivel.cc.auth.wrapper;

import com.swivel.cc.auth.domain.BaseDto;
import com.swivel.cc.auth.domain.response.ResponseDto;
import com.swivel.cc.auth.enums.ResponseStatusType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SendEmailResponseWrapper  implements BaseDto {

    private ResponseStatusType status;
    private String message;
    private String displayMessage;

    @Override
    public String toLogJson() {
        return toJson();
    }
}
