package com.swivel.cc.auth.configuration;


import com.google.analytics.data.v1beta.BetaAnalyticsDataClient;
import com.swivel.cc.auth.exception.AuthServiceException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Class to handle Google Analytics API's
 */

@Configuration
public class GoogleAnalyticsReportConfiguration {

    /**
     * Initializes an Analytics data api v1.
     *
     * @return analytics data client.
     */
    @Bean
    public BetaAnalyticsDataClient createAnalyticsConnection() {
        try {
            return BetaAnalyticsDataClient.create();
        } catch (IOException e) {
            throw new AuthServiceException("Initializing google analytics failed", e);
        }
    }
}

