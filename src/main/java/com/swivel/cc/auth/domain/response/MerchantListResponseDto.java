package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.MerchantBankSearchIndex;
import lombok.Getter;
import lombok.Setter;

/**
 * Merchant list response Dto
 */
@Getter
@Setter
public class MerchantListResponseDto implements ResponseDto {

    private BasicUserResponseDto profile;
    private BasicBusinessResponseDto business;

    public MerchantListResponseDto(MerchantBankSearchIndex merchantBankSearchIndex, String timeZone) {
        this.profile = new BasicUserResponseDto(merchantBankSearchIndex, timeZone);
        this.business = new BasicBusinessResponseDto(merchantBankSearchIndex);
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
