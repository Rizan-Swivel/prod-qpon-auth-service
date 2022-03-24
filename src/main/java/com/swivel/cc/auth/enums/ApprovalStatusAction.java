package com.swivel.cc.auth.enums;

import lombok.Getter;

/**
 * ApprovalStatusAction
 */
@Getter
public enum ApprovalStatusAction {
    BLOCKED_UNBLOCK,
    UNBLOCKED_BLOCK,
    APPROVED_BLOCK,
    PENDING_APPROVE

}
