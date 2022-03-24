package com.swivel.cc.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * ResourceBundleMessageSourceBean
 */
@Configuration
public class ResourceBundleMessageSourceBean {

    /**
     * Loads the message bundle properties
     *
     * @return ResourceBundleMessageSource
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasenames("success", "error", "email_details",
                "notification_email_templates", "notification_sms_templates");
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

}
