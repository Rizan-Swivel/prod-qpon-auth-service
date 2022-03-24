package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.domain.response.ReportDateResponseDto;
import com.swivel.cc.auth.enums.ReportDateOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Business profile views
 */
@Getter
@Setter
@AllArgsConstructor
public class BusinessProfileViews {

    private String userId;
    private long viewCount;
    private String displayDate;

    public BusinessProfileViews(String userId, Long viewCount, String displayDate, ReportDateOption dateOption,
                                long startDate, long endDate, String timeZone) {
        this.userId = userId;
        this.displayDate =
                new ReportDateResponseDto(dateOption, displayDate, startDate, endDate, timeZone).getDisplayDate();
        this.viewCount = viewCount;
    }

    public BusinessProfileViews(String userId, Long viewCount) {
        this.userId = userId;
        this.viewCount = viewCount;
    }

}
