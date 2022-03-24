package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User Details DTO for Response
 */
@Getter
@NoArgsConstructor
public class UserDetailResponseDto implements ResponseDto {

    private String fullName;
    private String email;
    private MobileNoResponseDto mobileNo;
    private String imageUrl;
    private Language language;
    private boolean isRegisteredUser;
    private ApprovalStatus approvalStatus;
    private boolean isActive;

    public UserDetailResponseDto(User user) {
        if (user != null) {
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.mobileNo = new MobileNoResponseDto(user.getMobileNo());
            this.imageUrl = user.getImageUrl();
            this.language = user.getLanguage();
            this.isRegisteredUser = user.isRegisteredUser();
            this.approvalStatus = user.getApprovalStatus();
            this.isActive = true;
        }
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
