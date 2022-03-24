package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * All bank list response Dto
 */
@Getter
@Setter
public class AllBankListResponseDto extends PageResponseDto {
    private final List<UserResponseDto> banks;

    public AllBankListResponseDto(Page<User> page, String timeZone) {
        super(page);
        this.banks = convertToAllBankListResponseDto(page, timeZone);
    }

    /**
     * Convert bank detail page into responseDto list.
     *
     * @param bankPage page
     * @return list of banks.
     */
    private List<UserResponseDto> convertToAllBankListResponseDto(Page<User> bankPage, String timeZone) {
        List<UserResponseDto> responseList = new ArrayList<>();
        bankPage.getContent().forEach(user -> responseList.add(new UserResponseDto(user, timeZone)));
        return responseList;
    }
}
