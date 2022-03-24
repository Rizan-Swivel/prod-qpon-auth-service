package com.swivel.cc.auth.domain.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequestDto_ extends RequestDto {

    private MobileNoRequestDto mobileNo;
    private String email;
    private String password;

    @Override
    public boolean isRequiredAvailable() {
        if (mobileNo != null) {
            return mobileNo.isRequiredAvailable() && isNonEmpty(password);
        } else {
            return isNonEmpty(email) && isNonEmpty(password);
        }
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
