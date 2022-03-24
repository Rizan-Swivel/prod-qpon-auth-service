package com.swivel.cc.auth.service;

import com.swivel.cc.auth.configuration.CustomTokenEnhancer;
import com.swivel.cc.auth.domain.TokenResponse;
import com.swivel.cc.auth.domain.entity.Resource;
import com.swivel.cc.auth.domain.entity.Role;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.repository.ResourceRepository;
import com.swivel.cc.auth.wrapper.RoleResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CustomTokenService {

    private final DefaultTokenServices authTokenServices;
    private final JdbcTokenStore jdbcTokenStore;
    private final String clientId;
    private final int tokenValidity;
    private final ResourceRepository resourceRepository;

    @Autowired
    public CustomTokenService(DefaultTokenServices authTokenServices,
                              JdbcTokenStore jdbcTokenStore,
                              @Value("${application.client-id}") String clientId,
                              @Value("${application.token-validity}") int tokenValidity,
                              ResourceRepository resourceRepository) {
        this.authTokenServices = authTokenServices;
        this.jdbcTokenStore = jdbcTokenStore;
        this.clientId = clientId;
        this.tokenValidity = tokenValidity;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Generate bearer token for social sign in
     *
     * @param user user
     * @return tokenResponse
     */
    public TokenResponse generateToken(User user) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Role role = user.getRole();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));

        role.getPermissions().forEach(permission ->
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName())));

        // set scopes if have any
        Set<String> scopes = new HashSet<>();

        //set resource Id's
        Set<String> resourceIdSet = new HashSet<>();

        //checks the client id in token validation
        OAuth2Request authorizationRequest = new OAuth2Request(null, clientId, grantedAuthorities,
                true, scopes, resourceIdSet, "", null, null);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getId(), null, grantedAuthorities);

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);
        authTokenServices.setAccessTokenValiditySeconds(tokenValidity);

        OAuth2AccessToken accessToken = authTokenServices.createAccessToken(authenticationRequest);

        // This block of logs to be removed after verification
        log.info("Access Token {} ", accessToken.getValue());
        log.info("Expire IN .......... {} ", accessToken.getExpiration());
        log.info("Refresh token -------- {} ", accessToken.getRefreshToken().getValue());


        List<Resource> allResource = getAllResource();

        jdbcTokenStore.storeAccessToken(accessToken, authenticationRequest);
        return new TokenResponse(accessToken, user, allResource);
    }

    /**
     * Get all resources
     *
     * @return list of resources
     */
    private List<Resource> getAllResource() {
        try {
            return resourceRepository.findAll();
        } catch (DataAccessException e) {
            log.error("ERROR_TOKEN", e);
            throw new AuthServiceException("ERrror");
        }
    }


    /**
     * Use this if need to set authorization parameters
     *
     * @return
     */
    private HashMap<String, String> setAuthorizationParameters() {
        HashMap<String, String> authorizationParameters = new HashMap<>();
        authorizationParameters.put("scope", "read");
        authorizationParameters.put("username", "user");
        authorizationParameters.put("client_id", "tokomobile");
        authorizationParameters.put("grant", "password");
        return authorizationParameters;
    }

}
