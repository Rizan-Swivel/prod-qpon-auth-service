package com.swivel.cc.auth.domain.response;

import lombok.Getter;

import java.util.List;

/**
 * Top 10 business profile views Dto
 */
@Getter
public class TopTenBusinessProfileViewsDto implements ResponseDto {

    private final List<BusinessProfileViewsResponseDto> views;

    public TopTenBusinessProfileViewsDto(List<BusinessProfileViewsResponseDto> businessProfileViews) {
        this.views = businessProfileViews;
    }

    /**
     * This method converts object to json string for logging purpose.
     * PII data should be obfuscated.
     *
     * @return json string
     */
    @Override
    public String toLogJson() {
        return toJson();
    }
}
