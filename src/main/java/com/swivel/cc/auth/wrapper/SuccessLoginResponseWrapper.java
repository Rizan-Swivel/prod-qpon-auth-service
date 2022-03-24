package com.swivel.cc.auth.wrapper;

import com.swivel.cc.auth.enums.ResponseStatusType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.domain.response.SocialSignInResponseDto;
import lombok.Getter;

/**
 * Login Success Response Wrapper
 */
@Getter
public class SuccessLoginResponseWrapper extends ResponseWrapper {


    private String access_token;
    private String token_type = "bearer";
    private String refresh_token;
    private String scope = "READ WRITE UPDATE";
    private String displayMessage;

    public SuccessLoginResponseWrapper(SuccessResponseStatusType successResponseStatusType, String message,
                                       SocialSignInResponseDto data) {
        super(ResponseStatusType.SUCCESS, successResponseStatusType.getMessage(), data);
        this.access_token = data.getAccessToken();
        this.refresh_token = data.getRefreshToken();
        this.displayMessage = message;
    }
}
