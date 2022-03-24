package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Bulk merchant response Dto
 */
@Getter
@Setter
public class BulkMerchantResponseDto implements ResponseDto {

    private List<BasicMerchantResponseDto> merchants;

    public BulkMerchantResponseDto(List<Business> businessList, String timeZone, RoleType roleType) {
        this.merchants = toBulkMerchantResponseDto(businessList, timeZone, roleType);
    }

    /**
     * This method is used to convert business list into response list.
     *
     * @param businessList businessList
     * @param timeZone     timeZone
     * @return merchants detail list.
     */
    private List<BasicMerchantResponseDto> toBulkMerchantResponseDto(List<Business> businessList, String timeZone,
                                                                     RoleType roleType) {
        List<BasicMerchantResponseDto> responseList = new ArrayList<>();
        for (Business business : businessList) {
            responseList.add(new BasicMerchantResponseDto(business, timeZone, roleType));
        }
        return responseList;
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
