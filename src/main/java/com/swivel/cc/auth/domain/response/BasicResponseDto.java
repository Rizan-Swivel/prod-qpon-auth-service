package com.swivel.cc.auth.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Basic details response Dto
 */
@Getter
@Setter
@AllArgsConstructor
public class BasicResponseDto implements ResponseDto {

    private String email;

    @Override
    public String toLogJson() {
        return toJson();
    }
}