package com.swivel.cc.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    public static final String REGISTRATION_ENDPOINT = "/api/v1/users/{type}";
    public static final String RESET_PASSWORD_ENDPOINT = "/api/v1/users/password/reset";
    private static final String GET_UPDATED_MOBILE_NO_ENDPOINT = "/api/v1/users/mobile-no/updates/**";
    private static final String SIGN_OUT = "/api/v1/users/sign-out";
    private static final String GOOGLE = "/api/v1/google/**";
    private static final String FACEBOOK = "/api/v1/facebook/sign-in";
    private static final String FACEBOOK_SIGNUP = "/api/v1/facebook/signup";
    private static final String FORGET_PASSWORD = "/api/v1/users/forgot-password";
    private static final String ADMIN_URLS = "/api/v1/admin/**";
    private static final String BULK_MERCHANT_ENDPOINT = "/api/v1/users/{userType}/bulk-info";

    /**
     * Configure a resource id for resource server api's
     *
     * @param resources resource
     */
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources.resourceId("api");
//    }

    /**
     * This method allow the API endpoints which not needs to be secured
     *
     * @param http http
     * @throws Exception exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, REGISTRATION_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.POST, GOOGLE).permitAll()
                .antMatchers(HttpMethod.PUT, RESET_PASSWORD_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET, GET_UPDATED_MOBILE_NO_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET, SIGN_OUT).permitAll()
                .antMatchers(HttpMethod.POST, FACEBOOK).permitAll()
                .antMatchers(HttpMethod.POST, FACEBOOK_SIGNUP).permitAll()
                .antMatchers(HttpMethod.POST, FORGET_PASSWORD).permitAll()
                .antMatchers(HttpMethod.POST, BULK_MERCHANT_ENDPOINT).permitAll()
                .antMatchers(ADMIN_URLS).access("hasAuthority('ADMIN')")
                .anyRequest().authenticated().and().cors().and()
                .csrf().disable();
    }
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers("/test").permitAll().anyRequest().authenticated().and().csrf().disable();
//    }

    //Todo: remove before production
    // For develop mode only to remove cors
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
