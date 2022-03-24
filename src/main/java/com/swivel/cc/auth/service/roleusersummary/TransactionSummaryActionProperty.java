package com.swivel.cc.auth.service.roleusersummary;

import com.swivel.cc.auth.configuration.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

/**
 * This class holds all property values relevant for the transaction summary action.
 */
@AllArgsConstructor
@Getter
@Setter
public class TransactionSummaryActionProperty {

    private static final String CREDIT_AND_DEBIT = "CREDIT_AND_DEBIT";
    private static final String SUMMARY_LABEL_TEMPLATE = "SUMMARY_LABEL_TEMPLATE";
    private static final String STATUS_KEY = "<status>";
    private static final String TYPE_KEY = "<type>";
    private String bookId;
    private Pageable pageable;
    private String value;
    private String timeZone;

    public String getDefaultDisplayLabel(Translator translator) {
//        String statusText = status.getDisplayName(translator);
//        String typeText = type == null ? translator.toLocale(CREDIT_AND_DEBIT) :
//                type.getDisplayNamePlural(translator);
//        return translator.toLocale(SUMMARY_LABEL_TEMPLATE).replace(STATUS_KEY, statusText).replace(TYPE_KEY, typeText);
        return "";
    }
}