package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.domain.entity.MerchantBankSearchIndex;
import com.swivel.cc.auth.enums.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Basic business response Dto
 */
@Getter
@Setter
public class BasicBusinessResponseDto implements ResponseDto {

    private String id;
    private String name;
    private ApprovalStatus approvalStatus;
    private String imageUrl;

    public BasicBusinessResponseDto(MerchantBankSearchIndex merchantBankSearchIndex) {
        this.id = merchantBankSearchIndex.getBusinessId();
        this.name = merchantBankSearchIndex.getBusinessName();
        this.approvalStatus = merchantBankSearchIndex.getBusinessApprovalStatus();
        this.imageUrl = merchantBankSearchIndex.getBusinessImageUrl();
    }

    public BasicBusinessResponseDto(Business business) {
        this.id = business.getId();
        this.name = business.getBusinessName();
        this.approvalStatus = business.getApprovalStatus();
        this.imageUrl = business.getImageUrl();
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
