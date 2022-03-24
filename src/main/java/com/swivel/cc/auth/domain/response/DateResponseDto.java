package com.swivel.cc.auth.domain.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Date response dto
 */

@Getter
@Setter
@NoArgsConstructor
public class DateResponseDto implements ResponseDto {

    private static final String DATE_FORMAT = "dd MMM yyyy"; //Eg: 24 Feb 2020
    private static final String DATE_TIME_FORMAT = "dd MMM yyyy, hh:mm a"; //Eg: 24 Feb 2020, 10:05AM
    private static final String EMPTY_STRING = "";
    private static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;

    private long milliseconds;
    private String displayDate;
    private String displayDateTime;
    private String displayText;

    public DateResponseDto(long timestamp, String timezoneId, String displayText) {
        this.milliseconds = timestamp;
        this.displayDate = timestamp > 0 ? getDisplayDate(timezoneId, milliseconds) : EMPTY_STRING;
        this.displayDateTime = timestamp > 0 ? getDisplayDateTime(timezoneId, milliseconds) : EMPTY_STRING;
        this.displayText = displayText != null ? displayText : EMPTY_STRING;
    }

    public DateResponseDto(long timestamp, String timezoneId, Date date) {
        this.milliseconds = timestamp;
        this.displayDate = timestamp > 0 ? getDisplayDate(timezoneId, milliseconds) : EMPTY_STRING;
        this.displayDateTime = timestamp > 0 ? getDisplayDateTime(timezoneId, milliseconds) : EMPTY_STRING;
        boolean moreThanDay = isMoreThanDay(timestamp, timezoneId);
        if (moreThanDay) {
            this.displayText = getDisplayDateTime(timezoneId, milliseconds);
        } else {
            var prettyTime = new PrettyTime();
            this.displayText = prettyTime.format(date);
        }
    }

    public DateResponseDto(long timestamp, String timezoneId, Date date, String prefix) {
        this.milliseconds = timestamp;
        this.displayDate = timestamp > 0 ? getDisplayDate(timezoneId, milliseconds) : EMPTY_STRING;
        this.displayDateTime = timestamp > 0 ? getDisplayDateTime(timezoneId, milliseconds) : EMPTY_STRING;
        boolean moreThanDay = isMoreThanDay(timestamp, timezoneId);
        if (moreThanDay) {
            this.displayText = prefix + this.displayDate;
        } else {
            var prettyTime = new PrettyTime();
            this.displayText = prefix + prettyTime.format(date);
        }
    }

    /**
     * Check the timestamp exceeds 24 hours
     *
     * @param timestamp  timestamp
     * @param timezoneId timezoneId
     * @return true/false
     */
    private boolean isMoreThanDay(long timestamp, String timezoneId) {
        var zoneId = ZoneId.of(timezoneId);
        ZonedDateTime zdt = LocalDate.now(zoneId).atTime(LocalTime.now()).atZone(zoneId);
        long l = zdt.toInstant().toEpochMilli();
        return Math.abs(timestamp - l) > MILLIS_PER_DAY;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }

    public void setDisplayDateTime(String displayDateTime) {
        this.displayDateTime = displayDateTime;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    /**
     * This method creates the display date.
     *
     * @param timezoneId timezoneId
     * @param timestamp  UTC timestamp in milliseconds
     * @return display date
     */
    private String getDisplayDateTime(String timezoneId, long timestamp) {
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        var sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(timezoneId));
        return sdf.format(calendar.getTime());
    }

    /**
     * This method creates the display date.
     *
     * @param timezoneId timezoneId
     * @param timestamp  UTC timestamp in milliseconds
     * @return display date
     */
    private String getDisplayDate(String timezoneId, long timestamp) {
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        var sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(timezoneId));
        return sdf.format(calendar.getTime());
    }
}
