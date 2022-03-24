package com.swivel.cc.auth.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.Language;
import com.swivel.cc.auth.wrapper.RoleResponseWrapper;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Data implements Serializable {

    private String userId;
    private String fullName;
    private String imageUrl;
    private Language language;
    private String accessToken;
    private String tokenType;
    private ApprovalStatus approvalStatus;
    private boolean isActive;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BusinessProfile businessProfile;
    private RoleResponseWrapper role;
}