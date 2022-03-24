package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.enums.Language;
import com.swivel.cc.auth.domain.TokenResponse;
import com.swivel.cc.auth.wrapper.RoleResponseWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User DTO for Response
 */
@Getter
@Setter
@NoArgsConstructor
public class SocialSignInResponseDto implements ResponseDto {

    private String userId;
    private Language language;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "bearer";
    private RoleResponseWrapper role;
//    private String message ="Successfully logged-in the user";
//    private String displayMessage ="Successfully logged-in the user.";
//    private String status= "SUCCESS";


    public SocialSignInResponseDto(User user, TokenResponse tokenResponse) {
        this.userId = user.getId();
        this.language = user.getLanguage();
        this.accessToken = tokenResponse.getAccessToken();
        this.refreshToken = tokenResponse.getRefreshToken();
        this.role = tokenResponse.getRole();
    }

    @Override
    public String toLogJson() {
        return toJson();
    }

}
