package com.swivel.cc.auth.enums;

import com.swivel.cc.auth.exception.InvalidDateOptionException;
import lombok.Getter;

/**
 * Graph date options
 */
@Getter
public enum GraphDateOption {
    TODAY("TODAY"),
    YESTERDAY("YESTERDAY"),
    THIS_WEEK("THIS-WEEK"),
    THIS_MONTH("THIS-MONTH"),
    THIS_YEAR("THIS-YEAR");
    private static final String INVALID_DATE_OPTION_TYPE = "Invalid graph date Option.";

    private final String option;

    GraphDateOption(String option) {
        this.option = option;
    }

    /**
     * This method returns relevant date option.
     *
     * @param type type
     * @return date option
     */
    public static GraphDateOption getOption(String type) {
        if (type != null) {
            for (GraphDateOption graphDateOption : GraphDateOption.values()) {
                if (graphDateOption.option.equalsIgnoreCase(type.trim())) {
                    return graphDateOption;
                }
            }
        }
        throw new InvalidDateOptionException(INVALID_DATE_OPTION_TYPE);
    }
}