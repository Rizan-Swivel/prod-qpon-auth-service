package com.swivel.cc.auth.enums;

/**
 * Enum values for response
 */
public enum ResponseStatusType {

    SUCCESS("Success"),
    ERROR("Error");

    private String status;

    ResponseStatusType(String status) {
        this.status = status;
    }

}
