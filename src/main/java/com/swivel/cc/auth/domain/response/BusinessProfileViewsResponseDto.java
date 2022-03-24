package com.swivel.cc.auth.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swivel.cc.auth.domain.entity.BankBusiness;
import com.swivel.cc.auth.domain.entity.Business;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessProfileViewsResponseDto implements ResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MerchantBusinessDto merchant;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MerchantBusinessDto bank;
    private long viewCount;

    public BusinessProfileViewsResponseDto(long viewCount, Business business) {
        this.viewCount = viewCount;
        this.merchant = new MerchantBusinessDto(business);
    }

    public BusinessProfileViewsResponseDto(long viewCount, BankBusiness bankBusiness) {
        this.viewCount = viewCount;
        this.bank = new MerchantBusinessDto(bankBusiness);
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
