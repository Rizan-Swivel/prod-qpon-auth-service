package com.swivel.cc.auth.enums;

import lombok.Getter;

/**
 * Approval status types
 */
@Getter
public enum ApprovalStatus {
    PENDING(null, null, null),
    APPROVED("APPROVE-MERCHANT", "APPROVE-MERCHANT-SUBJECT", "APPROVE-MERCHANT-BODY"),
    REJECTED("REJECT-MERCHANT", "REJECT-MERCHANT-SUBJECT", "REJECT-MERCHANT-BODY"),
    BLOCKED("BLOCK-MERCHANT", "BLOCK-MERCHANT-SUBJECT", "BLOCK-MERCHANT-BODY"),
    UNBLOCKED("UNBLOCK-MERCHANT", "UNBLOCK-MERCHANT-SUBJECT", "UNBLOCK-MERCHANT-BODY"),
    REJECT_PROFILE_UPDATE("REJECT-MERCHANT-PROFILE-UPDATE", "REJECT-MERCHANT-PROFILE-UPDATE-SUBJECT",
            "REJECT-MERCHANT-PROFILE-UPDATE-BODY"),
    APPROVE_PROFILE_UPDATE("APPROVE-MERCHANT-PROFILE-UPDATE", "APPROVE-MERCHANT-PROFILE-UPDATE-SUBJECT",
            "APPROVE-MERCHANT-PROFILE-UPDATE-BODY");

    private final String sms;
    private final String emailSubject;
    private final String emailBody;


    ApprovalStatus(String sms, String emailSubject, String emailBody) {
        this.sms = sms;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
    }
}
