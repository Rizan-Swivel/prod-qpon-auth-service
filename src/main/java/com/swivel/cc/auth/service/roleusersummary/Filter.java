package com.swivel.cc.auth.service.roleusersummary;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.service.TransactionSummaryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This class is used to generate summary data for role type - Filter
 */
@AllArgsConstructor
public class Filter implements RoleUserSummaryAction {

    private static final String LABEL_TEMPLATE = "FILTER_LABEL_TEMPLATE";
    private static final String START_DATE = "<start_date>";
    private static final String END_DATE = "<end_date>";
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final String DATE_STRING = "<date> 00:00:00";
    private static final String DATE_KEY = "<date>";
    private static final String SPACE = " ";
    private static final String ZERO = "0";

    private static final long MILLISECONDS_PER_DAY = 86400000;
    private TransactionSummaryActionProperty transactionSummaryActionProperty;
    private TransactionSummaryService transactionSummaryService;

    /**
     * This method returns page of transactions for transaction summary option - Filter.
     *
     * @return page of transactions
     */
    @Override
    public Page<User> getUsers() {
//        String timeZone = transactionSummaryActionProperty.getTimeZone();
//        LocalDateDuration duration = new LocalDateDuration(transactionSummaryActionProperty.getValue(),
//                timeZone);
//        return transactionSummaryService.filterTransactionsForDuration(
//                transactionSummaryActionProperty.getBookId(),
//                transactionSummaryActionProperty.getPageable(),
//                transactionSummaryActionProperty.getType(),
//                transactionSummaryActionProperty.getStatus().getActive(),
//                toDuration(duration, timeZone));
        return null;
    }

    /**
     * This method returns transaction summary display label for filter.
     *
     * @return display label
     */
    @Override
    public String getDisplayLabel(Translator translator) {
//        String base = transactionSummaryActionProperty.getDefaultDisplayLabel(translator);
//        LocalDateDuration duration = new LocalDateDuration(transactionSummaryActionProperty.getValue(),
//                transactionSummaryActionProperty.getTimeZone());
//        String startDate = localDateToString(duration.getStart());
//        String endDate = localDateToString(duration.getEnd());
//        return base +
//                translator.toLocale(LABEL_TEMPLATE)
//                        .replace(START_DATE, startDate)
//                        .replace(END_DATE, endDate);
        return "";
    }

//    /**
//     * This method converts LocalDateDuration to Duration object.
//     *
//     * @param duration LocalDateDuration
//     * @return Duration
//     */
//    private Duration toDuration(LocalDateDuration duration, String timeZone) {
//        String startDate = DATE_STRING.replace(DATE_KEY, duration.getStart().toString());
//        String endDate = DATE_STRING.replace(DATE_KEY, duration.getEnd().toString());
//        return new Duration(getUTCTimestamp(startDate, timeZone),
//                getUTCTimestamp(endDate, timeZone) + MILLISECONDS_PER_DAY);
//    }
//
//    /**
//     * This method returns relevant utc time stamp for the given date string.
//     *
//     * @param dateInString dateInString
//     * @return utc time stamp
//     */
//    private long getUTCTimestamp(String dateInString, String timeZone) {
//        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
//        try {
//            Date date = sdf.parse(dateInString);
//            return date.getTime();
//        } catch (ParseException e) {
//            throw new TokoBookException("Parsing date was failed for " + dateInString, e);
//        }
//    }
//
//    /**
//     * This method converts local date to the string.
//     * format: day month year (Eg: 04 Feb 2020)
//     *
//     * @param localDate localDate
//     * @return formatted date
//     */
//    private String localDateToString(LocalDate localDate) {
//        int year = localDate.getYear();
//        String month = localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
//        String day = localDate.getDayOfMonth() < 10 ? ZERO + localDate.getDayOfMonth()
//                : String.valueOf(localDate.getDayOfMonth());
//        return day + SPACE + month + SPACE + year;
//    }
//
//    /**
//     * This class is used to hold value for duration of LocalDate format.
//     */
//    @Getter
//    private class LocalDateDuration {
//        private static final String DASH = "-";
//        private LocalDate start;
//        private LocalDate end;
//
//        public LocalDateDuration(String value, String timeZone) {
//            String[] splitValue = value.split(DASH);
//            long startMilliseconds = Long.parseLong(splitValue[1]);
//            long endMilliseconds = Long.parseLong(splitValue[2]);
//            this.start =
//                    Instant.ofEpochMilli(startMilliseconds).atZone(ZoneId.of(timeZone)).toLocalDate();
//            this.end =
//                    Instant.ofEpochMilli(endMilliseconds).atZone(ZoneId.of(timeZone)).toLocalDate();
//        }
//    }
}
