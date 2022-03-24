package com.swivel.cc.auth.domain.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequestDto extends RequestDto {

    private String token;
    private String password;

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(token);
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
