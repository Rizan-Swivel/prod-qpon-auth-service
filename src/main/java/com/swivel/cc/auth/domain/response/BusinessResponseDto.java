package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.domain.request.MobileNoRequestDto;
import com.swivel.cc.auth.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Business details response Dto
 */
@Getter
@Setter
@AllArgsConstructor
public class BusinessResponseDto implements ResponseDto {

    private String businessId;
    private String merchantId;
    private String businessName;
    private String ownerName;
    private ApprovalStatus approvalStatus;
    private MobileNoRequestDto mobileNo;
    private MobileNoResponseDto telephone;
    private String businessRegNo;
    private String address;
    private String email;
    private String imageUrl;
    private String webSite;
    private String facebook;
    private String instagram;
    private DateResponseDto createdAt;
    private DateResponseDto updatedAt;
    private boolean isActive;
    private String profileType;

    public BusinessResponseDto(Business business, String timeZone) {
        this.businessId = business.getId();
        this.merchantId = business.getMerchant().getId();
        this.businessName = business.getBusinessName();
        this.ownerName = business.getOwnerName();
        this.mobileNo = new MobileNoRequestDto(business.getMobileNo());
        this.telephone = new MobileNoResponseDto(business.getTelephone());
        this.email = business.getEmail();
        this.businessRegNo = business.getBusinessRegNo();
        this.address = business.getAddress();
        this.imageUrl = business.getImageUrl();
        this.webSite = business.getWebSite();
        this.facebook = business.getFacebook();
        this.instagram = business.getInstagram();
        this.createdAt = new DateResponseDto(business.getCreatedAt().getTime(), timeZone, business.getCreatedAt());
        this.updatedAt = new DateResponseDto(business.getUpdatedAt().getTime(), timeZone, business.getUpdatedAt());
        this.approvalStatus = business.getApprovalStatus();
        this.isActive = checkMerchantIsActive(business.getMerchant().getApprovalStatus(), business.getApprovalStatus());
        this.profileType = business.getMerchant().getRole().getName();
    }

    /**
     * This method is used to check merchant is active or not.
     *
     * @param userApprovalStatus     user profile approval status.
     * @param businessApprovalStatus business profile approval status.
     * @return true/false
     */
    private boolean checkMerchantIsActive(ApprovalStatus userApprovalStatus, ApprovalStatus businessApprovalStatus) {
        return businessApprovalStatus == ApprovalStatus.APPROVED &&
                (userApprovalStatus == ApprovalStatus.APPROVED || userApprovalStatus == ApprovalStatus.UNBLOCKED);
    }

    @Override
    public String toLogJson() {
        return toJson();
    }

}