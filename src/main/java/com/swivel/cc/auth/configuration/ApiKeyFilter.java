package com.swivel.cc.auth.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/api/v1/users/mobile-no/updates/*", "/api/v1/users/MERCHANT/bulk-info"})
public class ApiKeyFilter extends GenericFilterBean {

    private static final String APPLICATION_HEADER = "app-key";
    private final String applicationKey;

    public ApiKeyFilter(@Value("${spring.application.app-key}") String applicationKey) {
        this.applicationKey = applicationKey;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String appKey = request.getHeader(APPLICATION_HEADER);
        if (appKey == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing app key");
        }
        if (appKey != null && !appKey.equals(applicationKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid app key");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
