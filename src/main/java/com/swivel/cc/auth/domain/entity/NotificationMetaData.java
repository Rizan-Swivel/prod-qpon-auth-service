package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationMetaData {

    private ApprovalStatus approvalStatus;
    private User merchant;
    private String timeZone;


}
