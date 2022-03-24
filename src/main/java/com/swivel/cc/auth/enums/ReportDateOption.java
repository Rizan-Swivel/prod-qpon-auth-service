package com.swivel.cc.auth.enums;

import lombok.Getter;

/**
 * Report date options
 */
@Getter
public enum ReportDateOption {

    DAILY("date", "yyyyMMdd", "d MMM yyyy"),
    WEEKLY("week", "yyyyww", "WW MMM yyyy"),
    MONTHLY("nthMonth", "yyyy MM", "MMM yyyy"),
    YEARLY("year", "yyyy", "yyyy");

    private final String analyticsOption;
    private final String dateFromFormat;
    private final String dateToFormat;

    ReportDateOption(String analyticsOption, String dateFromFormat, String dateToFormat) {
        this.analyticsOption = analyticsOption;
        this.dateFromFormat = dateFromFormat;
        this.dateToFormat = dateToFormat;
    }
}