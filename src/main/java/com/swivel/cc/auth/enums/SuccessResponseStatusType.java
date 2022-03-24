package com.swivel.cc.auth.enums;


import lombok.Getter;

/**
 * Success Response Status Type
 */
@Getter
public enum SuccessResponseStatusType {

    UPDATE_USER(2000, "Successfully updated the user"),
    UPDATE_EMAIL(2001, "Successfully updated email address"),
    UPDATE_MOBILE_NO(2002, "Successfully updated mobile number"),
    CREATE_USER(2003, "Successfully created the user"),
    GET_USER(2004, "Successfully returned user"),
    UPDATE_PASSWORD(2005, "Successfully updated password"),
    RESET_PASSWORD(2006, "Successfully reset the password"),
    LOGGED_IN(2007, "Successfully logged-in the user"),
    LOGGED_OUT(2008, "Successfully logged-out the user"),
    GET_USER_UPDATED_MOBILE_NO(2009, "Success returned updated mobile numbers"),
    VALID_TOKEN(2010, "Valid user token."),
    CREATE_ROLE(2011, "Role created successfully"),
    GET_ALL_PERMISSIONS(2012, "Successfully returned permissions"),
    GET_ROLE(2013, "Successfully returned the role"),
    DELETE_ROLE(2014, "Role removed successfully"),
    UPDATE_ROLE(2015, "Role updated successfully"),
    GET_ROLE_SUMMARY(2016, "Successfully returned the role summary"),
    GET_ROLE_USER_SUMMARY(2017, "Successfully returned the role user summary"),
    UPDATE_ROLE_USER(2018, "Successfully updated the role user"),
    GET_USERS(2019, "Successfully returned users."),
    GET_PENDING_MERCHANTS(2021, "Successfully returned pending merchants."),
    BLOCKED_OR_UNBLOCKED_MERCHANT(2022, "Successfully blocked/unblocked merchant."),
    APPROVED_OR_REJECTED_MERCHANT(2023, "Successfully approved/rejected merchant."),
    GET_MERCHANT(2024, "Successfully returned merchant."),
    CREATED_BUSINESS_PROFILE(2025, "Successfully created business profile"),
    GET_BUSINESS_PROFILE(2026, "Successfully returned business profile"),
    ADDED_MERCHANT_CONTACT(2027, "Successfully added merchant contact data"),
    GET_CONTACT_INFO(2028, "Successfully returned merchant contact data"),
    NO_CONTACT_INFO_FOUND(2029, "Add contact information for merchant, " +
            "no existing contact information available."),
    NO_BUSINESS_INFO_FOUND(2030, "Add business information for merchant, " +
            "no existing business information available."),
    GET_PENDING_BUSINESS_INFO(2031, "Successfully returned pending business information"),
    GET_PENDING_CONTACT_INFO(2032, "Successfully returned pending contact information"),
    GET_BUSINESS_PROFILE_VIEWS(2033, "Successfully returned business profile views."),
    GET_TODAY_SUMMARY(2034, "Successfully returned today's summary."),
    PASSWORD_RESET_TOKEN(2035, "Password reset token has been sent."),
    GET_BANK_LIST(2036, "Successfully returned bank list.");


    private final String code;
    private final String message;

    SuccessResponseStatusType(int code, String message) {
        this.code = getCodeString(code);
        this.message = message;
    }

    /**
     * Return String value of code to match with resource-message property key
     *
     * @param code code
     * @return code
     */
    public String getCodeString(int code) {
        return String.valueOf(code);
    }
}
