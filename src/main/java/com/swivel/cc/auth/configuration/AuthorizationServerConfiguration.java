package com.swivel.cc.auth.configuration;

import com.swivel.cc.auth.repository.ResourceRepository;
import com.swivel.cc.auth.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final String IS_AUTHENTICATED = "isAuthenticated()";
    private static final String PERMISSION_ALL = "permitAll()";

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailService;
    private final ResourceRepository resourceRepository;
    private final MerchantService merchantService;

    @Autowired
    public AuthorizationServerConfiguration(DataSource dataSource, PasswordEncoder passwordEncoder,
                                            AuthenticationManager authenticationManager,
                                            UserDetailsService userDetailService,
                                            ResourceRepository resourceRepository, MerchantService merchantService) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.resourceRepository = resourceRepository;
        this.merchantService = merchantService;
    }

    // Configure the security of the Authorization Server, which means in practical terms the /oauth/token endpoint.
    // Expose /oauth/check_token endpoint

    /**
     * Configure the security of the Authorization Server
     *
     * @param serverSecurityConfigurer security
     * @throws Exception exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer serverSecurityConfigurer) throws Exception {
        serverSecurityConfigurer.checkTokenAccess(IS_AUTHENTICATED).tokenKeyAccess(PERMISSION_ALL);
    }

    //Configure the ClientDetailsService, e.g. declaring individual clients and their properties
    // password grant is no enabled by default need to provide an AuthenticationManager to
    // configure(AuthorizationServerEndpointsConfigurer)

    /**
     * Client Details configurer
     *
     * @param clients clients
     * @throws Exception exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

    //Configure the non-security features of the Authorization Server endpoints, like token store, token customizations,
    // user approvals and grant types. You shouldn't need to do anything by default, unless you need password grants,
    // in which case you need to provide an AuthenticationManager

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) throws Exception {
        endpointsConfigurer.userDetailsService(userDetailService);
        endpointsConfigurer.tokenEnhancer(tokenEnhancer());
        endpointsConfigurer.tokenStore(tokenStore());
        endpointsConfigurer.authenticationManager(authenticationManager);

        endpointsConfigurer.exceptionTranslator(exception -> {

            if (exception instanceof InvalidGrantException) {
                OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
                return ResponseEntity
                        .status(oAuth2Exception.getHttpErrorCode())
                        .body(new CustomLoginException(oAuth2Exception.getMessage()));
            }

            if (exception instanceof OAuth2Exception) {
                OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
                return ResponseEntity
                        .status(oAuth2Exception.getHttpErrorCode())
                        .body(new CustomOauthException(oAuth2Exception.getMessage()));
            } else {
                throw exception;
            }
        });

    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer(resourceRepository, merchantService);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    JdbcTokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
