package com.swivel.cc.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto extends RequestDto {

    private String userId;
    private String fullName;
    private String imageUrl;

    /**
     * This  method trims the full name.
     *
     * @param fullName full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName.trim();
    }

    /**
     * This method trims the imageUrl.
     *
     * @param imageUrl image url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl.trim();
    }

    /**
     * This method checks all required fields are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(userId);
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
