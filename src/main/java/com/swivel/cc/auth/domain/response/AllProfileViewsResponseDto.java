package com.swivel.cc.auth.domain.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * All profile views response Dto
 */
@Getter
@Setter
public class AllProfileViewsResponseDto extends PageResponseDto {

    private List<BusinessProfileViewsResponseDto> views;

    public AllProfileViewsResponseDto(Page<BusinessProfileViewsResponseDto> page,
                                      List<BusinessProfileViewsResponseDto> views) {
        super(page);
        this.views = views;
    }
}
