package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

/**
 * Basic merchant response Dto
 */
@Getter
@Setter
public class BasicMerchantResponseDto {

    private static final String JOINED_ON = "Joined on ";
    private String id;
    private String name;
    private String imageUrl;
    private ApprovalStatus approvalStatus;
    private boolean isActive;
    private RoleType userType;
    private DateResponseDto joinedOn;

    public BasicMerchantResponseDto(Business approvedBusiness, String timeZone, RoleType roleType) {
        this.id = approvedBusiness.getMerchant().getId();
        this.name = approvedBusiness.getBusinessName();
        this.imageUrl = approvedBusiness.getImageUrl();
        this.approvalStatus = approvedBusiness.getApprovalStatus();
        this.isActive = checkMerchantIsActive(approvedBusiness);
        this.userType = roleType;
        this.joinedOn = new DateResponseDto(approvedBusiness.getMerchant().getCreatedAt().getTime(), timeZone,
                approvedBusiness.getMerchant().getCreatedAt(), JOINED_ON);
    }

    /**
     * This method is used to check merchant is active or not.
     *
     * @param approvedBusiness business
     * @return true/false
     */
    private boolean checkMerchantIsActive(Business approvedBusiness) {
        ApprovalStatus userApprovalStatus = approvedBusiness.getMerchant().getApprovalStatus();
        ApprovalStatus businessApprovalStatus = approvedBusiness.getApprovalStatus();
        return businessApprovalStatus == ApprovalStatus.APPROVED &&
                (userApprovalStatus == ApprovalStatus.APPROVED || userApprovalStatus == ApprovalStatus.UNBLOCKED);
    }
}
