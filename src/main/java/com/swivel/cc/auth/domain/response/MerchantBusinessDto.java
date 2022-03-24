package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.BankBusiness;
import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.enums.ApprovalStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantBusinessDto implements ResponseDto {

    private String id;
    private String businessName;
    private ApprovalStatus approvalStatus;
    private String imageUrl;

    public MerchantBusinessDto(Business business) {
        this.id = business.getMerchant().getId();
        this.businessName = business.getBusinessName();
        this.approvalStatus = business.getMerchant().getApprovalStatus();
        this.imageUrl = business.getImageUrl();
    }

    public MerchantBusinessDto(BankBusiness bankBusiness) {
        this.id = bankBusiness.getBank().getId();
        this.businessName = bankBusiness.getBusinessName();
        this.approvalStatus = bankBusiness.getBank().getApprovalStatus();
        this.imageUrl = bankBusiness.getImageUrl();
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
