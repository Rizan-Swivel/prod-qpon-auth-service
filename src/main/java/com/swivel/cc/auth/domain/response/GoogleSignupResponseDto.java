package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.enums.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User DTO for Response
 */
@Getter
@Setter
@NoArgsConstructor
public class GoogleSignupResponseDto implements ResponseDto {

    private String userId;
    private Language language;

    public GoogleSignupResponseDto(User user) {
        this.userId = user.getId();
        this.language = user.getLanguage();
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
