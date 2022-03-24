package com.swivel.cc.auth.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Today summary response Dto
 */
@Getter
@AllArgsConstructor
public class TodaySummaryResponseDto implements ResponseDto {

    private int noOfNewMerchants;
    private int totalMerchants;
    private int totalActiveMerchants;
    private int noOfNewMobileUsers;
    private int totalMobileUsers;
    private int noOfNewBanks;
    private int totalBanks;
    private int totalActiveBanks;

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
