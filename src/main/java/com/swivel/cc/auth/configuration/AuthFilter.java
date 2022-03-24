package com.swivel.cc.auth.configuration;

import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.util.FilterErrorResponseGenerator;
import com.swivel.cc.auth.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthFilter
 */
@Configuration
@Slf4j
public class AuthFilter implements Filter {

    private static final String FACEBOOK_TOKEN_ENDPOINT = "/oauth/token/facebook";
    private static final String TOKEN_ENDPOINT = "/oauth/token";
    private static final String USER_NAME = "username";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String PSW_FIELD = "password";
    private static final String GRANT_TYPE = "grant_type";

    private final Validator validator;
    private final FilterErrorResponseGenerator errorResponseGenerator;

    @Autowired
    public AuthFilter(Validator validator, FilterErrorResponseGenerator errorResponseGenerator) {
        this.validator = validator;
        this.errorResponseGenerator = errorResponseGenerator;
    }

    /**
     * Registering the token endpoint to be filtered
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<AuthFilter> loginFilter() {
        FilterRegistrationBean<AuthFilter> authFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        authFilterFilterRegistrationBean.setFilter(new AuthFilter(validator, errorResponseGenerator));
        authFilterFilterRegistrationBean.addUrlPatterns(FACEBOOK_TOKEN_ENDPOINT, TOKEN_ENDPOINT);
        return authFilterFilterRegistrationBean;
    }

    /**
     * Validates the login request fields
     *
     * @param servletRequest  request
     * @param servletResponse response
     * @param filterChain     filterChain
     * @throws IOException      IOException
     * @throws ServletException ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        log.debug("Executing the login filter");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getServletPath().equals(FACEBOOK_TOKEN_ENDPOINT)) {
            if (isNonEmpty(request.getParameter(ACCESS_TOKEN))) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                errorResponseGenerator.sendErrorResponse(response,
                        ErrorResponseStatusType.MISSING_REQUIRED_FIELDS, HttpStatus.BAD_REQUEST);
            }
        }

        if (request.getServletPath().equals(TOKEN_ENDPOINT) && request.getParameter(GRANT_TYPE).equals(PSW_FIELD)) {
            if (isNonEmpty(request.getParameter(USER_NAME)) && isNonEmpty(request.getParameter(PSW_FIELD))) {
                if (validator.isValidPassword(request.getParameter(PSW_FIELD))) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else
                    errorResponseGenerator.sendErrorResponse(response,
                            ErrorResponseStatusType.INVALID_USERNAME_OR_PASS, HttpStatus.BAD_REQUEST);
            } else {
                errorResponseGenerator.sendErrorResponse(response,
                        ErrorResponseStatusType.MISSING_REQUIRED_FIELDS, HttpStatus.BAD_REQUEST);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * Check for empty fields
     *
     * @param field field
     * @return true/false
     */
    protected boolean isNonEmpty(String field) {
        log.debug("Executing the login filter - isNonEmpty");
        return field != null && !field.trim().isEmpty();
    }

}
