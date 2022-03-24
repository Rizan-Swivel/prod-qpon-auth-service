package com.swivel.cc.auth.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupedReportRequestDto extends RequestDto {

    private Long startDate;
    private Long endDate;

    @Override
    public boolean isRequiredAvailable() {
        return startDate != null && endDate != null;
    }

    /**
     * This method checks start and end dates.
     *
     * @return true/false
     */
    public boolean isValidDateRange() {
        return startDate <= endDate;
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
