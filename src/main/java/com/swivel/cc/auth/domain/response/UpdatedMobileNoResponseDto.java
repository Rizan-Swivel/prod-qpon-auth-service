package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.domain.entity.UserMobileNo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UpdatedMobileNo DTO for Response
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdatedMobileNoResponseDto implements ResponseDto {

    private String userId;
    private String mobileNo;

    @Override
    public String toLogJson() {
        return toJson();
    }

    public UpdatedMobileNoResponseDto(UserMobileNo userMobileNo) {
        this.userId = userMobileNo.getUserId();
        this.mobileNo = userMobileNo.getMobileNo();
    }
}
