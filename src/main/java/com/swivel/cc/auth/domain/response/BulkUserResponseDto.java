package com.swivel.cc.auth.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkUserResponseDto implements ResponseDto {

    private List<BasicUserResponseDto> users;

    public BulkUserResponseDto(List<BasicUserResponseDto> users) {
        this.users = users;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
