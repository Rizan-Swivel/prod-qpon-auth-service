package com.swivel.cc.auth.controller;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class SignOutController extends Controller {


    private static final String AUTHORIZATION = "Authorization";
    private static final String PREFIX = "bearer";
    private final DefaultTokenServices defaultTokenServices;

    @Autowired
    public SignOutController(Translator translator, @Qualifier("tokenServices")
                                         DefaultTokenServices defaultTokenServices) {
        super(translator);
        this.defaultTokenServices = defaultTokenServices;
    }

    /**
     * Revoke User Token
     * @param userId userId
     * @param accessToken accessToken
     * @return ResponseWrapper responseWrapper
     */
    @GetMapping(path = "/sign-out", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> signOut(@RequestHeader(value = HEADER_USER_ID) String userId,
                                                           @RequestHeader(value = AUTHORIZATION) String accessToken) {
        try {
            String tokenValue = accessToken.substring(PREFIX.length()).trim();
            defaultTokenServices.revokeToken(tokenValue);
            log.error("Successfully logged out userId: {}", userId);
            return getSuccessResponse(SuccessResponseStatusType.LOGGED_OUT, null);
        } catch (InvalidUserException e) {
            log.error("Getting user was failed for invalid user. userId: {}", userId, e);
            return getUnauthorizedError(ErrorResponseStatusType.UNAUTHORIZED);
          } catch (AuthServiceException e) {
            log.error("Getting user was failed for userId: {}", userId, e);
            return getInternalServerError();
        }
    }

}
