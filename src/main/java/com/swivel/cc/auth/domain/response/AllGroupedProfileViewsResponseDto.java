package com.swivel.cc.auth.domain.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * All grouped profile views response dto
 */
@Getter
public class AllGroupedProfileViewsResponseDto extends PageResponseDto {

    private final List<GroupedProfileViewsResponseDto> views;

    public AllGroupedProfileViewsResponseDto(Page<GroupedProfileViewsResponseDto> page,
                                             List<GroupedProfileViewsResponseDto> views) {
        super(page);
        this.views = views;
    }
}
