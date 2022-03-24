package com.swivel.cc.auth.controller;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.domain.request.GoogleSignupRequestDto;
import com.swivel.cc.auth.domain.response.SocialSignInResponseDto;
import com.swivel.cc.auth.domain.response.GoogleSignupResponseDto;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.exception.NoUserException;
import com.swivel.cc.auth.exception.UserAlreadyExistsException;
import com.swivel.cc.auth.service.GoogleAuthService;
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
@RequestMapping("/api/v1/google")
public class GoogleController extends Controller {

    private final GoogleAuthService googleAuthService;

    @Autowired
    public GoogleController(GoogleAuthService googleAuthService, Translator translator) {
        super(translator);
        this.googleAuthService = googleAuthService;
    }

    /**
     * This method creates a user.
     *
     * @param userRequestDto user
     * @return userId and language
     */
    @PostMapping(path = "/signup", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody GoogleSignupRequestDto userRequestDto) {
        try {
            if (userRequestDto.isRequiredAvailable()) {
                User user = googleAuthService.createUser(userRequestDto.getIdToken());
                GoogleSignupResponseDto googleSignupResponseDto = new GoogleSignupResponseDto(user);
                return getSuccessResponse(SuccessResponseStatusType.CREATE_USER, googleSignupResponseDto);
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
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
    public ResponseEntity<ResponseWrapper> login(@RequestBody GoogleSignupRequestDto userRequestDto) {
        try {
            if (userRequestDto.isRequiredAvailable()) {
                SocialSignInResponseDto user = googleAuthService.loginUser(userRequestDto.getIdToken());
                return getSuccessLoginResponse(SuccessResponseStatusType.LOGGED_IN, user);
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

//    @GetMapping(path = "/sign-out", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
//    public ResponseEntity<ResponseWrapper> getUserByUserId(@RequestHeader(value = X_USER_ID) String userId,
//                                                           @RequestHeader(value = AUTHORIZATION) String accessToken) {
//        // you can get the token details by user this
//        //OAuth2Authentication authentication  - parameter
//        //authentication.getUserAuthentication().getPrincipal()
//        try {
//            log.error("Successfully logged out token: {}", accessToken);
//            log.error("Successfully logged out userId: {}", userId);
//            return getSuccessResponse(SuccessResponseStatusType.LOGGED_OUT, null);
//        } catch (AuthServiceException e) {
//            log.error("Getting user was failed for userId: {}", userId, e);
//            return getInternalServerError();
//        }
//    }


}
