package com.swivel.cc.auth.enums;

import lombok.Getter;

@Getter
public enum SendEmailDetails {

    SUBJECT("Forget password link", "e001"),
    PASSWORD_RESET_MSG("Reset password", "e002");

    private final String displayMessage;
    private final String code;

    SendEmailDetails(String displayMessage, String code) {
        this.displayMessage = displayMessage;
        this.code = code;
    }
}
