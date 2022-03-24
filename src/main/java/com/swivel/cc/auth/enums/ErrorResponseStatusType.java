package com.swivel.cc.auth.enums;

import lombok.Getter;

/**
 * Error response status type
 */
@Getter
public enum ErrorResponseStatusType {

    MISSING_REQUIRED_FIELDS(4000, "Required fields are missing"),
    INVALID_USERNAME_OR_PASS(4105, "Invalid username or password"),
    INVALID_USER_ID(4001, "Invalid user id"),
    INVALID_USER_TYPE(4011, "Invalid user type"),
    UNSUPPORTED_USER_TYPE(4012, "Unsupported user type"),
    INVALID_ACTION(4013, "Invalid action"),
    INVALID_USER_TOKEN(4002, "Invalid user token"),
    EXISTING_USER(4003, "Already existing user"),
    NO_USER(4004, "Invalid user"),
    NO_EMAIL_ACCESS(4005, "Required permission to access the email address"),
    INVALID_TIMEZONE(4100, "Invalid time zone."),
    INVALID_MOBILE_NO(4101, "Invalid mobile number"),
    INVALID_PASSWORD(4104, "Invalid password"),
    EXISTING_EMAIL(4107, "Already existing email"),
    EXISTING_MOBILE_NO(4106, "Already existing mobile no"),
    INVALID_EMAIL(4103, "Invalid email address"),
    OLD_PASSWORD_DOES_NOT_MATCH(4108, "Old password does not match"),
    INVALID_LANGUAGE(4109, "Invalid language"),
    INTERNAL_SERVER_ERROR(5000, "Failed due to an internal server error"),
    UNAUTHORIZED(401, "you don't have permission to access this resource"),
    INVALID_IMAGE_URL(4903, "Invalid image URL"),
    NO_USER_FOUND(4908, "No user found"),
    INVALID_LOGIN(4910, "Incorrect username or password."),
    MULTIPLE_TYPES_NOT_SUPPORTED(4400, "Multiple types are not supported"),
    PASSWORD_RESET_TOKEN_WAS_EXPIRED(4401, "The password reset token is expired, please resubmit new request"),
    TOKEN_EXPIRED(4402, "Token Expired"),
    EXISTING_ROLE(4403, "Already existing role"),
    INVALID_ROLE(4404, "Invalid role"),
    FORBIDDEN_ACTION_ROLE(4405, "Cannot delete default role"),
    FAILED_TO_SENT_EMAIL(4406, "Failed to sent password reset link"),
    MAX_USER_REQUEST_COUNT(4407, "Exceeded maximum user details."),
    NO_MERCHANT_FOUND(4408, "No merchant found"),
    FAILED_TO_GET_MERCHANT(4409, "Failed to get merchant"),
    INVALID_MERCHANT_ID(4410, "Invalid merchant id"),
    INVALID_WEBSITE_URL(4411, "Invalid website url"),
    INVALID_FACEBOOK_URL(4412, "Invalid facebook url"),
    INVALID_INSTAGRAM_URL(4413, "Invalid instagram url"),
    INVALID_TELEPHONE_NUMBER(4414, "Invalid telephone number."),
    INVALID_BUSINESS_ID(4415, "Invalid business id"),
    INVALID_CONTACT_ID(4416, "Invalid contact id"),
    INVALID_DATE_RANGE(4417, "Start date should be less than or equal to end date"),
    INVALID_DATE_OPTION(4418, "Invalid date option"),
    UNSUPPORTED_OPTION_FOR_DATE_RANGE(4419, "Unsupported option for date range"),
    PENDING_MERCHANT(4420, "Merchant account creation is in pending"),
    INVALID_URLS(4421, "Invalid URL for website or facebook or instagram."),
    NO_APPROVED_BUSINESS_INFO_FOUND(4422, "Approved business information are not available for the merchant.."),
    INVALID_MOBILE_NUMBER(4423, "Invalid mobile number."),
    FAILED_TO_SEND_PW_RESET_TOKEN(4424, "Failed to send the password reset token."),
    INVALID_USER_ID_OR_ROLE(4425, "Invalid userId or userRole.");

    private final int code;
    private final String message;

    ErrorResponseStatusType(int code, String message) {
        this.code = code;
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
