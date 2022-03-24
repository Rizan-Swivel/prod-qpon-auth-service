package com.swivel.cc.auth.domain.request;

import com.swivel.cc.auth.enums.ReportDateOption;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * Report request dto
 */
@Setter
@Getter
public class ReportRequestDto extends RequestDto {

    private ReportDateOption option;
    private Long startDate;
    private Long endDate;

    @Override
    public boolean isRequiredAvailable() {
        return option != null && startDate != null && endDate != null;
    }

    /**
     * This method is used to validate date option with selected date range.
     *
     * @return true/false
     */
    public boolean isSupportedDateRange() {
        long dateDifference = TimeUnit.DAYS.convert(endDate - startDate, TimeUnit.MILLISECONDS);
        return ((dateDifference < 30 && (option == ReportDateOption.DAILY || option == ReportDateOption.WEEKLY)) ||
                (dateDifference >= 30 && dateDifference < 366 && option == ReportDateOption.MONTHLY) ||
                (dateDifference >= 30 && option == ReportDateOption.YEARLY));
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
