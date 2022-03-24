package com.swivel.cc.auth.util;

import com.swivel.cc.auth.enums.GraphDateOption;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * This class covert Graph Date Option into Date Range
 */
@Getter
public class DateRangeConverter {

    private final String timeZone;
    private final DayOfWeek firstDayOfWeek;
    private final GraphDateOption graphDateOption;
    private String startDate;
    private String endDate;

    public DateRangeConverter(String timeZone, GraphDateOption graphDateOption) {
        this.timeZone = timeZone;
        this.graphDateOption = graphDateOption;
        this.firstDayOfWeek = WeekFields.of(Locale.US).getFirstDayOfWeek();
        createDateRange();
    }

    private LocalDate getFirstDay() {
        return LocalDate.now(ZoneId.of(timeZone)).with(TemporalAdjusters.previousOrSame(this.firstDayOfWeek));
    }

    /**
     * This method create date range using GraphDateOption
     */
    public void createDateRange() {
        switch (graphDateOption) {
            case YESTERDAY:
                this.startDate = String.valueOf(LocalDate.now().minusDays(1));
                this.endDate = String.valueOf(LocalDate.now());
                break;
            case THIS_WEEK:
                this.startDate = String.valueOf(getFirstDay());
                this.endDate = String.valueOf(LocalDate.now());
                break;
            case THIS_MONTH:
                this.startDate = String.valueOf(LocalDate.now().withDayOfMonth(1));
                this.endDate = String.valueOf(LocalDate.now());
                break;
            case THIS_YEAR:
                this.startDate = String.valueOf(LocalDate.now().withDayOfYear(1));
                this.endDate = String.valueOf(LocalDate.now());
                break;
            default:
                this.startDate = String.valueOf(LocalDate.now());
                this.endDate = String.valueOf(LocalDate.now());
                break;
        }
    }

}