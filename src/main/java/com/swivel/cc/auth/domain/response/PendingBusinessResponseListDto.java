package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.BankBusiness;
import com.swivel.cc.auth.domain.entity.Business;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Pending business response list dto
 */
@Getter
public class PendingBusinessResponseListDto extends PageResponseDto {

    private final List<PendingBusinessResponseDto> allPendingBusinessInfo;

    public PendingBusinessResponseListDto(Page<Business> page, String timeZone) {
        super(page);
        this.allPendingBusinessInfo = addToPendingBusinessResponseListDto(page, timeZone);
    }

    public PendingBusinessResponseListDto(String timeZone, Page<BankBusiness> page) {
        super(page);
        this.allPendingBusinessInfo = addToPendingBankBusinessResponseListDto(page, timeZone);
    }

    /**
     * Add merchant business information from page to dto list.
     *
     * @param page     page
     * @param timeZone timeZone
     * @return businessResponseList
     */
    private List<PendingBusinessResponseDto> addToPendingBusinessResponseListDto(Page<Business> page, String timeZone) {
        List<PendingBusinessResponseDto> responseList = new ArrayList<>();
        page.getContent().forEach(business -> responseList.add(new PendingBusinessResponseDto(business, timeZone)));
        return responseList;
    }

    /**
     * Add bank business information from page to dto list.
     *
     * @param page     page
     * @param timeZone timeZone
     * @return businessResponseList
     */
    private List<PendingBusinessResponseDto> addToPendingBankBusinessResponseListDto(Page<BankBusiness> page,
                                                                                     String timeZone) {
        List<PendingBusinessResponseDto> responseList = new ArrayList<>();
        page.getContent().forEach(business -> responseList.add(new PendingBusinessResponseDto(business, timeZone)));
        return responseList;
    }
}
