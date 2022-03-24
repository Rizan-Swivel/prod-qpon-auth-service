package com.swivel.cc.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordUpdateRequestDto extends RequestDto {

    private String userId;
    private String password;
    private String newPassword;

    /**
     * This method checks all required fields are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(userId) && isNonEmpty(password) && isNonEmpty(newPassword);
    }

    /**
     * This method converts this object to json string for logging purpose.
     * All fields are obfuscated.
     *
     * @return json string
     */
    @Override
    public String toLogJson() {
        return toJson();
    }
}
