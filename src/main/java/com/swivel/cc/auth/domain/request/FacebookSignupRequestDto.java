package com.swivel.cc.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacebookSignupRequestDto extends RequestDto {

    private String accessToken;

    /**
     * This method checks all required fields are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        return  isNonEmpty(accessToken);
    }

    /**
     * This method converts this object to json string for logging purpose.
     * All fields are obfuscated.
     *
     * @return json string
     */
    @Override
    public String toLogJson() {
        //Todo: mask the token
        return toJson();
    }
}
