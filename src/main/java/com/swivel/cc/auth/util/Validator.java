package com.swivel.cc.auth.util;

import org.springframework.stereotype.Component;

/**
 * This class is used to validate fields.
 */
@Component
public class Validator {

    private static final String EMAIL_REGEX = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
    private static final String MOBILE_NO_REGEX = "^\\+(\\d{2}[-])\\d{9}$";
    private static final String PASS_REGEX = "^((?!.*[\\s])(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,40})";
    private static final String URL_REGEX = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";


    /**
     * This method validates a given email.
     *
     * @param email email address
     * @return true/ false
     */
    public boolean isValidEmail(String email) {
        return email != null && !email.isBlank() && email.trim().matches(EMAIL_REGEX);
    }

    /**
     * This method validates a given mobile no with country code.
     *
     * @param mobileNo mobile no
     * @return true/ false
     */
    public boolean isValidMobileNoWithCountryCode(String mobileNo) {
        return mobileNo.matches(MOBILE_NO_REGEX);
    }


    /**
     * This method validates a given password.
     * Password must contain at least:
     * 1 Uppercase
     * 1 Lowercase
     * 1 Number
     * 6 characters
     *
     * @param password password
     * @return true/ false
     */
    public boolean isValidPassword(String password) {
        return password.matches(PASS_REGEX);
    }

    /**
     * This method validates a given username is a valid email/ mobile no.
     *
     * @param username username
     * @return true/ false
     */
    public boolean isValidUsername(String username) {
        return isValidMobileNoWithCountryCode(username) || isValidEmail(username);
    }

    /**
     * This method checks the validity of the given url.
     *
     * @param url url
     * @return true/ false
     */
    public boolean isValidUrl(String url) {
        return url != null && !url.isBlank() && url.trim().matches(URL_REGEX);
    }

}
