package com.swivel.cc.auth.controller;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.service.UserService;
import com.swivel.cc.auth.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/token")
public class TokenController extends Controller {

    private static final String AUTHORIZATION = "Authorization";
    private static final String PREFIX = "Bearer";
    private final UserService userService;

    public TokenController(Translator translator, UserService userService) {
        super(translator);
        this.userService = userService;
    }

    /**
     * This method returns a User
     *
     * @param userId userId
     * @return User
     */
    @GetMapping(path = "/validate", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> validateToken(@RequestHeader(value = HEADER_USER_ID) String userId,
                                                         @RequestHeader(value = AUTHORIZATION) String accessToken) {
        try {
            boolean isExpired = userService.readAccessToken(accessToken.substring(PREFIX.length()).trim());
            if (isExpired) {
                return getBadRequestError(ErrorResponseStatusType.TOKEN_EXPIRED);
            }
            return getSuccessResponse(SuccessResponseStatusType.VALID_TOKEN, null);
        } catch (InvalidUserException e) {
            log.error("Getting user was failed for invalid user. userId: {}", userId, e);
            return getUnauthorizedError(ErrorResponseStatusType.UNAUTHORIZED);
        } catch (AuthServiceException e) {
            log.error("Validating token was failed for userId: {}", userId, e);
            return getInternalServerError();
        }
    }
}
