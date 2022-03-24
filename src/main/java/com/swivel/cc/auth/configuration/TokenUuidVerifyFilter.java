package com.swivel.cc.auth.configuration;

import com.swivel.cc.auth.domain.AuthUserDetail;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.util.FilterErrorResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
public class TokenUuidVerifyFilter implements Filter {

    private static final String UPDATE_ENDPOINT = "/api/v1/users";
    private static final String UPDATE_EMAIL = "/api/v1/users/email";
    private static final String UPDATE_MOBILE = "/api/v1/users/mobileNo";
    private static final String UPDATE_PSW = "/api/v1/users/password";
    private static final String HEADER_USER_ID = "User-Id";

    private final FilterErrorResponseGenerator errorResponseGenerator;

    @Autowired
    public TokenUuidVerifyFilter(FilterErrorResponseGenerator errorResponseGenerator) {
        this.errorResponseGenerator = errorResponseGenerator;
    }

    /**
     * Registering the token endpoint to be filtered
     *
     * @return FilterRegistrationBean
     */
    @Bean(name = "filterRegistrationBean2")
    public FilterRegistrationBean<TokenUuidVerifyFilter> updateFilter() {

        FilterRegistrationBean<TokenUuidVerifyFilter> updateFilterFilterRegistrationBean = new FilterRegistrationBean<>();
        updateFilterFilterRegistrationBean.setFilter(new TokenUuidVerifyFilter(errorResponseGenerator));
        updateFilterFilterRegistrationBean.addUrlPatterns(/*UPDATE_ENDPOINT,*/ UPDATE_EMAIL, UPDATE_MOBILE, UPDATE_PSW);
        return updateFilterFilterRegistrationBean;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String xUserId = request.getHeader(HEADER_USER_ID);

        if (request.getMethod().equals(RequestMethod.PUT.name())) {

            final OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder
                    .getContext().getAuthentication();

            Object principal = authentication.getUserAuthentication().getPrincipal();

            String userId = ((AuthUserDetail) principal).getId();

            if (!userId.equals(xUserId)) {
                errorResponseGenerator.sendErrorResponse(response, ErrorResponseStatusType.UNAUTHORIZED,
                        HttpStatus.UNAUTHORIZED);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }

        } else {
            log.debug("********** Omitting the Update filter **********");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
