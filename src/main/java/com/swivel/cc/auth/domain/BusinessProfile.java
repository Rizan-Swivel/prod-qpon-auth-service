package com.swivel.cc.auth.domain;

import com.swivel.cc.auth.enums.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Business profile details used at merchant login.
 */
@Getter
@Setter
public class BusinessProfile implements Serializable {

    private boolean isUpdated;
    private ApprovalStatus approvalStatus;
}
