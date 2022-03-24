package com.swivel.cc.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMobileUpdateRequestDto extends RequestDto {

    private String userId;
    private MobileNoRequestDto mobileNo;

    /**
     * This method checks all required fields are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        if (mobileNo != null) {
            return isNonEmpty(userId) && mobileNo.isRequiredAvailable();
        }
        return false;
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
