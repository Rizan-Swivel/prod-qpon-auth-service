package com.swivel.cc.auth.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swivel.cc.auth.domain.BaseDto;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.Language;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User DTO for Response
 */
@Getter
@NoArgsConstructor
public class UserResponseDto implements ResponseDto {

    private String userId;
    private String fullName;
    private String imageUrl;
    private String email;
    private MobileNoResponseDto mobileNo;
    private ApprovalStatus approvalStatus;
    private DateResponseDto createdAt;
    private DateResponseDto updatedAt;
    private Language language;
    @JsonIgnore
    private BasicUserLog basicUserLog;

    public UserResponseDto(User user, String timeZone) {
        this.userId = user.getId();
        this.fullName = user.getFullName();
        this.imageUrl = user.getImageUrl();
        this.email = user.getEmail();
        this.mobileNo = new MobileNoResponseDto(user.getMobileNo());
        this.approvalStatus = user.getApprovalStatus();
        this.createdAt = new DateResponseDto(user.getCreatedAt().getTime(), timeZone, user.getCreatedAt());
        this.updatedAt = new DateResponseDto(user.getUpdatedAt().getTime(), timeZone, user.getUpdatedAt());
        this.language = user.getLanguage();
        this.basicUserLog = new BasicUserLog(this);
    }

    @Override
    public String toLogJson() {
        return basicUserLog.toLogJson();
    }

    /**
     * This class is for logging purpose only.
     */
    @Getter
    private class BasicUserLog implements BaseDto {
        private final String userId;
        private final String fullName;
        private final ApprovalStatus approvalStatus;
        private final DateResponseDto createdAt;
        private final DateResponseDto updatedAt;
        private final Language language;

        public BasicUserLog(UserResponseDto userResponseDto) {
            this.userId = userResponseDto.getUserId();
            this.fullName = userResponseDto.getFullName();
            this.approvalStatus = userResponseDto.getApprovalStatus();
            this.createdAt = userResponseDto.getCreatedAt();
            this.updatedAt = userResponseDto.getUpdatedAt();
            this.language = userResponseDto.getLanguage();
        }

        @Override
        public String toLogJson() {
            return toJson();
        }
    }
}
