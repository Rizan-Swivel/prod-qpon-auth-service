package com.swivel.cc.auth.controller;

import com.swivel.cc.auth.configuration.Translator;

import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.domain.response.GoogleSignupResponseDto;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.exception.*;
import com.swivel.cc.auth.domain.request.FacebookSignupRequestDto;
import com.swivel.cc.auth.domain.response.SocialSignInResponseDto;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.service.FacebookAuthService;
import com.swivel.cc.auth.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/facebook")
public class FacebookController extends Controller {

    private final FacebookAuthService facebookAuthService;

    @Autowired
    public FacebookController(FacebookAuthService googleAuthService, Translator translator) {
        super(translator);
        this.facebookAuthService = googleAuthService;
    }

    /**
     * This method creates a user.
     *
     * @param userRequestDto user
     * @return userId and language
     */
    @PostMapping(path = "/signup", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody FacebookSignupRequestDto userRequestDto) {
        try {
            if (userRequestDto.isRequiredAvailable()) {
                User user = facebookAuthService.createUser(userRequestDto.getAccessToken());
                GoogleSignupResponseDto googleSignupResponseDto = new GoogleSignupResponseDto(user);
                return getSuccessResponse(SuccessResponseStatusType.CREATE_USER, googleSignupResponseDto);
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (NoEmailAccessException e) {
            log.error("No permission to access user's email address for user: {}", userRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.NO_EMAIL_ACCESS);
        } catch (UserAlreadyExistsException e) {
            log.error("User is already exist for user: {}", userRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.EXISTING_USER);
        } catch (InvalidUserException e) {
            log.error("Invalid user token for user: {} d", userRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_USER_TOKEN);
        } catch (AuthServiceException e) {
            log.error("Creating user was failed for user: {}", userRequestDto.toLogJson(), e);
            return getInternalServerError();
        }
    }

    /**
     * This method creates a user.
     *
     * @param userRequestDto user
     * @return userId and language
     */
    @PostMapping(path = "/sign-in", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> login(@RequestBody FacebookSignupRequestDto userRequestDto) {
        try {
            if (userRequestDto.isRequiredAvailable()) {
                SocialSignInResponseDto socialSignInResponseDto =
                        facebookAuthService.loginUser(userRequestDto.getAccessToken());
                return getSuccessLoginResponse(SuccessResponseStatusType.LOGGED_IN, socialSignInResponseDto);
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (NoUserException e) {
            log.error("Signed up user is not available: {}", userRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.NO_USER);
        } catch (InvalidUserException e) {
            log.error("Invalid user token for user: {} d", userRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_USER_TOKEN);
        } catch (AuthServiceException e) {
            log.error("Login user was failed for user: {}", userRequestDto.toLogJson(), e);
            return getInternalServerError();
        }
    }

}
