package com.swivel.cc.auth.domain.request;

import com.swivel.cc.auth.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

/**
 * User
 */
@Getter
@Setter
public class UserRequestDto extends RequestDto {

    public static final String EMAIL_KEY = "email";
    public static final String PASS_KEY = "password";
    public static final String MOBILE_NO_KEY = "mobile no";
    public static final String LANGUAGE_KEY = "language";

    private String fullName;
    private String email;
    private String password;
    private MobileNoRequestDto mobileNo;
    private String language = "English";
    private RoleType roleType = RoleType.USER;

    /**
     * This method checks all required fields are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        if (roleType != RoleType.ADMIN && mobileNo != null) {
            return isNonEmpty(fullName) && isNonEmpty(password) && mobileNo.isRequiredAvailable();
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
