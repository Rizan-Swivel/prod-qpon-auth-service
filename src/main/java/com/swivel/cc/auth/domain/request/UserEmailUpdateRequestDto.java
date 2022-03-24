package com.swivel.cc.auth.domain.request;

import com.swivel.cc.auth.util.Validator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailUpdateRequestDto extends RequestDto {

    private String userId;
    private String email;

    /**
     * This setter method trim the email when the object initiate.
     *
     * @param email email
     */
    public void setEmail(String email) {
        if (email != null && !email.isBlank()) {
            this.email = email.trim();
        } else {
            this.email = null;
        }
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
     * This method returns true when email is valid.
     *
     * @param validator validator
     * @return true/ false
     */
    public boolean validateEmail(Validator validator) {
        return email == null || email.equals("") || validator.isValidEmail(email);
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
