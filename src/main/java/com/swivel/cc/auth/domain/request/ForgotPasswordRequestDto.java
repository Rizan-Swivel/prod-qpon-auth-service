package com.swivel.cc.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgotPasswordRequestDto extends RequestDto {

    private MobileNoRequestDto mobileNo;

    @Override
    public String toLogJson() {
        return toJson();
    }
}
