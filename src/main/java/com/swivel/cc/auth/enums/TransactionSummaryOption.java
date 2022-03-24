package com.swivel.cc.auth.enums;

import com.swivel.cc.auth.exception.AuthServiceException;

/**
 * This holds types of transaction summary options.
 */
public enum TransactionSummaryOption {

    FILTER,
    SEARCH;

    private static final String INVALID_TRANSACTION_SUMMARY_OPTION = "Invalid transaction summary option";

    /**
     * This method validates a given transaction summary option.
     *
     * @param option transaction summary option
     * @return true/ false
     */
    public static boolean isValidOption(String option) {
        if (option != null) {
            for (TransactionSummaryOption o : TransactionSummaryOption.values()) {
                if (o.name().equalsIgnoreCase(option.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method returns relevant transaction action type.
     *
     * @param option transaction summary option
     * @return option
     */
    public static TransactionSummaryOption getOption(String option) {
        if (option != null) {
            for (TransactionSummaryOption o : TransactionSummaryOption.values()) {
                if (o.name().equalsIgnoreCase(option.trim())) {
                    return o;
                }
            }
        }
        throw new AuthServiceException(INVALID_TRANSACTION_SUMMARY_OPTION);
    }
}
