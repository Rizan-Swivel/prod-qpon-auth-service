package com.swivel.cc.auth.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.domain.entity.Contact;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Pending contact response Dto
 */
@Getter
@AllArgsConstructor
public class PendingContactResponseDto implements ResponseDto {

    private String contactId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BasicUserResponseDto merchant;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BasicUserResponseDto bank;
    private BasicBusinessResponseDto business;
    private ApprovalStatus approvalStatus;
    private DateResponseDto updatedAt;

    public PendingContactResponseDto(Contact contact, String timeZone, Business business) {
        this.contactId = contact.getId();
        if (contact.getMerchant().getRole().getId() == RoleType.MERCHANT.getId())
            this.merchant = new BasicUserResponseDto(contact.getMerchant(), timeZone);
        else
            this.bank = new BasicUserResponseDto(contact.getMerchant(), timeZone);
        this.business = new BasicBusinessResponseDto(business);
        this.approvalStatus = contact.getApprovalStatus();
        this.updatedAt = new DateResponseDto(contact.getUpdatedAt().getTime(), timeZone, contact.getUpdatedAt());
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
