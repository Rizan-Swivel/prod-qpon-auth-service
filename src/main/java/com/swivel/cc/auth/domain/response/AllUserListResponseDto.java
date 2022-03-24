package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * All user list response Dto
 */
@Getter
@Setter
public class AllUserListResponseDto extends PageResponseDto {

    private List<UserResponseDto> users;

    public AllUserListResponseDto(Page<User> page, String timeZone) {
        super(page);
        this.users = convertToAllUserListResponseDto(page, timeZone);
    }

    /**
     * Convert user detail page into UserAndMerchantDetailResponseDto list.
     *
     * @param userPage page
     * @return responseList list
     */
    private List<UserResponseDto> convertToAllUserListResponseDto(Page<User> userPage, String timeZone) {
        List<UserResponseDto> responseList = new ArrayList<>();
        userPage.getContent().forEach(user -> responseList.add(new UserResponseDto(user, timeZone)));
        return responseList;
    }
}
