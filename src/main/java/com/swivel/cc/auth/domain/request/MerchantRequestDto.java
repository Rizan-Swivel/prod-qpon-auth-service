package com.swivel.cc.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Merchant request Dto
 */
@Getter
@Setter
public class MerchantRequestDto extends RequestDto {

    private BasicInfoRequestDto basicDetails;
    private BusinessRequestDto businessDetails;
    private ContactRequestDto contactDetails;

    @Override
    public String toLogJson() {
        return toJson();
    }

}
