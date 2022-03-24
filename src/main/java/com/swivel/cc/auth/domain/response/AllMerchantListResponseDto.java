package com.swivel.cc.auth.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.swivel.cc.auth.domain.entity.MerchantBankSearchIndex;
import com.swivel.cc.auth.enums.RoleType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * All merchant list response dto
 */
@Getter
@Setter
public class AllMerchantListResponseDto extends PageResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MerchantListResponseDto> merchants;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MerchantListResponseDto> banks;

    public AllMerchantListResponseDto(Page<MerchantBankSearchIndex> page, String timeZone, RoleType roleType) {
        super(page);
        convertToAllMerchantListResponseDto(page, timeZone, roleType);
    }

    /**
     * Convert merchant detail page into MerchantListResponseDto list.
     *
     * @param merchantPage merchant page
     * @param timeZone     timeZone
     * @param roleType     Merchant/Bank
     */
    private void convertToAllMerchantListResponseDto(Page<MerchantBankSearchIndex> merchantPage,
                                                     String timeZone, RoleType roleType) {
        List<MerchantListResponseDto> responseList = new ArrayList<>();
        merchantPage.getContent().forEach(merchant -> responseList.add(new MerchantListResponseDto(merchant, timeZone)));
        if (roleType == RoleType.MERCHANT)
            this.merchants = responseList;
        else
            this.banks = responseList;
    }
}
