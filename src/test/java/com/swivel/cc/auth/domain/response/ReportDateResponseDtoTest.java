package com.swivel.cc.auth.domain.response;

import com.swivel.cc.auth.enums.ReportDateOption;
import com.swivel.cc.auth.exception.AuthServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link ReportDateResponseDto} class
 */
class ReportDateResponseDtoTest {

    private static final long startDate = 1611513000000L;
    private static final long endDate = 1642962600000L;
    private static final String TIME_ZONE = "Asia/Colombo";

    /**
     * Daily
     * date: 2021 01 01, given: 20210101, expect: 1 Jan 2021
     */
    @Test
    void Should_ReturnReportDateResponseDto_When_DailyOptionIsGiven() {
        ReportDateResponseDto reportDateResponseDto =
                new ReportDateResponseDto(ReportDateOption.DAILY, "20210101", startDate, endDate, TIME_ZONE);
        assertEquals("1 Jan 2021", reportDateResponseDto.getDisplayDate());
    }

    /**
     * Weekly
     * date: 2022 week 1, given: 01, expect: 5th week - Dec 2021
     */
    @Test
    void Should_ReturnReportDateResponseDto_When_WeeklyOptionIsGiven() {
        ReportDateResponseDto reportDateResponseDto =
                new ReportDateResponseDto(ReportDateOption.WEEKLY, "01", startDate, endDate, TIME_ZONE);
        assertEquals("5th week - Dec 2021", reportDateResponseDto.getDisplayDate());
    }

    /**
     * Monthly
     * date: 2021 January, given: 0000, expect: Jan 2021
     */
    @Test
    void Should_ReturnReportDateResponseDto_When_MonthlyOptionIsGiven() {
        ReportDateResponseDto reportDateResponseDto =
                new ReportDateResponseDto(ReportDateOption.MONTHLY, "0000", startDate, endDate, TIME_ZONE);
        assertEquals("Jan 2021", reportDateResponseDto.getDisplayDate());
    }

    /**
     * Yearly
     * year: 2021, given: 2021, expect: 2021
     */
    @Test
    void Should_ReturnReportDateResponseDto_When_YearlyOptionIsGiven() {
        ReportDateResponseDto reportDateResponseDto =
                new ReportDateResponseDto(ReportDateOption.YEARLY, "2021", startDate, endDate, TIME_ZONE);
        assertEquals("2021", reportDateResponseDto.getDisplayDate());
    }

    /**
     * Any other string
     * given: qwerty, expect: exception message
     */
    @Test
    void Should_ReturnReportDateResponseDto_When_RandomStringIsGiven() {
        Exception exception = assertThrows(AuthServiceException.class,
                () -> new ReportDateResponseDto(ReportDateOption.DAILY, "qwerty", startDate, endDate, TIME_ZONE));
        assertTrue(exception.getMessage().contains("Converting to display date text failed"));
    }
}