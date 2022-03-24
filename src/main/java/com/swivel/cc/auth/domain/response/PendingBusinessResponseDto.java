package com.swivel.cc.auth.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swivel.cc.auth.domain.entity.BankBusiness;
import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Pending business response Dto
 */
@Getter
@AllArgsConstructor
public class PendingBusinessResponseDto implements ResponseDto {

    private String businessId;
    private String businessName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BasicUserResponseDto merchant;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BasicUserResponseDto bank;
    private ApprovalStatus approvalStatus;
    private DateResponseDto updatedAt;

    public PendingBusinessResponseDto(Business business, String timeZone) {
        this.businessId = business.getId();
        this.businessName = business.getBusinessName();
        this.merchant = new BasicUserResponseDto(business.getMerchant(), timeZone);
        this.approvalStatus = business.getApprovalStatus();
        this.updatedAt = new DateResponseDto(business.getUpdatedAt().getTime(), timeZone, business.getUpdatedAt());
    }

    public PendingBusinessResponseDto(BankBusiness bankBusiness, String timeZone) {
        this.businessId = bankBusiness.getId();
        this.businessName = bankBusiness.getBusinessName();
        this.bank = new BasicUserResponseDto(bankBusiness.getBank(), timeZone);
        this.approvalStatus = bankBusiness.getApprovalStatus();
        this.updatedAt = new DateResponseDto(bankBusiness.getUpdatedAt().getTime(), timeZone, bankBusiness.getUpdatedAt());
    }

    /**
     * This method converts object to json string for logging purpose.
     * PII data should be obfuscated.
     *
     * @return json string
     */
    @Override
    public String toLogJson() {
        return toJson();
    }
}
