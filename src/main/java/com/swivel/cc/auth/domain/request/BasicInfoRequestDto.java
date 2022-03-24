package com.swivel.cc.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasicInfoRequestDto extends RequestDto{

    private String email;
    private String password;

    @Override
    public String toLogJson() {
        return toJson();
    }

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(email) && isNonEmpty(password);
    }
}
