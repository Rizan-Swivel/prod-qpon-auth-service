package com.swivel.cc.auth.domain.response;


import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.enums.Language;
import lombok.Getter;

/**
 * UserDetailByMobileResponseDto DTO for Response
 */
@Getter
public class UserDetailByMobileResponseDto implements ResponseDto {

    private String userId;
    private String fullName;
    private String email;
    private MobileNoResponseDto mobileNo;
    private String imageUrl;
    private Language language;
    private boolean isRegisteredUser;

    public UserDetailByMobileResponseDto(User user) {
        if (user != null) {
            this.userId = user.getId();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.mobileNo = new MobileNoResponseDto(user.getMobileNo());
            this.imageUrl = user.getImageUrl();
            this.language = user.getLanguage();
            this.isRegisteredUser = user.isRegisteredUser();
        }
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
