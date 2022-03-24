package com.swivel.cc.auth.enums;

import lombok.Getter;

/**
 * Analytics dimensions
 */
@Getter
public enum AnalyticsDimensionsAndMetrics {

    MERCHANT_ID_DIMENSION("customEvent:merchant_id"),
    BANK_ID_DIMENSION("customEvent:bank_id"),
    EVENT_COUNT_METRIC("eventCount");

    private final String analyticsDimension;

    AnalyticsDimensionsAndMetrics(String analyticsDimension) {
        this.analyticsDimension = analyticsDimension;
    }
}
