package com.swivel.cc.auth.controller;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.entity.MerchantBankSearchIndex;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.domain.entity.UserMobileNo;
import com.swivel.cc.auth.domain.request.*;
import com.swivel.cc.auth.domain.response.*;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.enums.Language;
import com.swivel.cc.auth.enums.RoleType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.exception.*;
import com.swivel.cc.auth.service.UserRoleService;
import com.swivel.cc.auth.service.UserService;
import com.swivel.cc.auth.util.Validator;
import com.swivel.cc.auth.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User Controller
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController extends Controller {

    private static final int MAX_USER_COUNT = 250;
    private static final String GETTING_USERS_FOR_INVALID_USER_ID = "Getting users for invalid userId: {}";
    private static final String ALREADY_EXISTING_MOBILE_NO = "Already existing mobile no : {}";
    private static final String TOKEN_REVOCATION_ERROR = "error occurred while removing access token";
    private final UserService userService;
    private final Validator validator;
    private final DefaultTokenServices defaultTokenServices;
    private final UserRoleService userRoleService;

    @Autowired
    public UserController(UserService userService, Validator validator,
                          @Qualifier("tokenServices") DefaultTokenServices defaultTokenServices, Translator translator,
                          UserRoleService userRoleService) {
        super(translator);
        this.userService = userService;
        this.validator = validator;
        this.defaultTokenServices = defaultTokenServices;
        this.userRoleService = userRoleService;
    }

    /**
     * This method creates a user.
     *
     * @param userRequestDto user
     * @return userId and language
     */
    @PostMapping(path = "/{type}", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> createUser(@RequestHeader(name = TIME_ZONE_HEADER) String timeZone,
                                                      @RequestBody UserRequestDto userRequestDto,
                                                      @PathVariable RoleType type) {
        userRequestDto.setRoleType(type);
        try {
            if (!isValidTimeZone(timeZone)) {
                log.debug("Invalid time zone. Time zone: {}", timeZone);
                return getBadRequestError(ErrorResponseStatusType.INVALID_TIMEZONE);
            }
            if (type == RoleType.ADMIN) {
                return getBadRequestError(ErrorResponseStatusType.UNSUPPORTED_USER_TYPE);
            }
            if (userRequestDto.isRequiredAvailable()) {
                if (isValidInput(userRequestDto)) {
                    User user = new User(userRequestDto);
                    return createUser(user, timeZone);
                } else {
                    return getBadRequestError(getCreateUserInvalidFields(userRequestDto));
                }
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (AuthServiceException e) {
            log.error("Creating user was failed for user: {}", userRequestDto.toLogJson(), e);
            return getInternalServerError();
        }
    }

    /**
     * This method update a user
     *
     * @param userUpdateRequestDto userUpdateRequestDto
     * @return user
     */
    @Secured({USER_ROLE, MERCHANT_ROLE})
    @PatchMapping(path = "", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                                      @RequestHeader(value = HEADER_USER_ID) String userId) {
        try {
            if (userUpdateRequestDto.isRequiredAvailable()) {
                if (isInvalidForOptionalImageUrl(userUpdateRequestDto.getImageUrl())) {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_IMAGE_URL);
                }
                return updateIfValidUser(userUpdateRequestDto);
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (AuthServiceException e) {
            log.error("Updating user was failed for userId: {}. User: {}", userId, userUpdateRequestDto.toLogJson(), e);
            return getInternalServerError();
        }
    }

    /**
     * This method returns a User
     *
     * @param userId userId
     * @return User
     */
//    @PreAuthorize("hasAuthority('invoke_api')")
    @GetMapping(path = "", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> getUserByUserId(@RequestHeader(value = HEADER_USER_ID) String userId) {
        // you can get the token details by user this
        //OAuth2Authentication authentication  - parameter
        //authentication.getUserAuthentication().getPrincipal()
        try {
            return fetchUserById(userId);
        } catch (AuthServiceException e) {
            log.error("Getting user was failed for userId: {}", userId, e);
            return getInternalServerError();
        }
    }

    /**
     * This method update the mobileNo of a user
     *
     * @param unregisterUserDto unregisterUserDto
     * @return userResponseDto
     */
    @PostMapping(path = "/mobileNo", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<ResponseWrapper> createUnregisteredUser(@RequestBody UnregisterUserDto unregisterUserDto,
                                                                  @RequestHeader(name = TIME_ZONE_HEADER) String timeZone) {

        try {
            if (!isValidTimeZone(timeZone)) {
                log.debug("Invalid time zone. Time zone: {}", timeZone);
                return getBadRequestError(ErrorResponseStatusType.INVALID_TIMEZONE);
            }
            if (unregisterUserDto.isRequiredAvailable()) {
                if (validator.isValidMobileNoWithCountryCode(unregisterUserDto.getMobileNo().getNo())) {
                    User user = new User(unregisterUserDto);
                    return createUnregisteredUserIfValidUser(unregisterUserDto, user, timeZone);
                } else {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_MOBILE_NO);
                }
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (AuthServiceException e) {
            log.error("Creating unregistered user was failed: {}", unregisterUserDto.toLogJson(), e);
            return getInternalServerError();
        }
    }

    /**
     * This method returns a User by mobile number
     *
     * @param mobileNo mobileNo
     * @return User
     */
    @GetMapping(path = "/mobileNo/{mobileNo}", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> getUserByMobileNo(@PathVariable("mobileNo") String mobileNo) {
        try {
            if (validator.isValidMobileNoWithCountryCode(mobileNo)) {
                return fetchUserByMobileNo(mobileNo);
            } else {
                return getBadRequestError(ErrorResponseStatusType.INVALID_MOBILE_NO);
            }
        } catch (AuthServiceException e) {
            log.error("Getting user was failed for mobileNo: {}", mobileNo, e);
            return getInternalServerError();
        }
    }

    /**
     * This method update the email address of a user
     *
     * @param emailUpdateRequestDto emailUpdateRequestDto
     * @return user
     */
    @Secured({USER_ROLE, MERCHANT_ROLE})
    @PutMapping(path = "/email", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> updateUserEmail(@RequestBody UserEmailUpdateRequestDto emailUpdateRequestDto,
                                                           @RequestHeader(value = HEADER_USER_ID) String userId) {
        try {
            if (emailUpdateRequestDto.isRequiredAvailable()) {
                if (emailUpdateRequestDto.validateEmail(validator)) {
                    return updateEmailForValidUser(emailUpdateRequestDto);
                } else {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_EMAIL);
                }
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (AuthServiceException e) {
            log.error("Updating email was failed for userId: {}, email: {}", userId, emailUpdateRequestDto.getEmail(), e);
            return getInternalServerError();
        }
    }

    /**
     * This method updates mobile No of a user
     *
     * @param userMobileUpdateRequestDto userMobileUpdateRequestDto
     * @return userDetailResponseDto
     */
    @Secured({USER_ROLE, MERCHANT_ROLE})
    @PutMapping(path = "/mobileNo", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<ResponseWrapper> updateMobileNumber(@RequestBody UserMobileUpdateRequestDto
                                                                      userMobileUpdateRequestDto,
                                                              @RequestHeader(value = HEADER_USER_ID) String userId) {
        try {
            if (userMobileUpdateRequestDto.isRequiredAvailable()) {
                if (validator.isValidMobileNoWithCountryCode(userMobileUpdateRequestDto.getMobileNo().getNo())) {
                    return updateMobileForValidUser(userMobileUpdateRequestDto);
                } else {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_MOBILE_NO);
                }
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (AuthServiceException e) {
            log.error("Updating mobile number was failed for userId: {}, mobile: {}", userId,
                    userMobileUpdateRequestDto.toLogJson(), e);
            return getInternalServerError();
        }
    }

    /**
     * This method update current password of a user
     *
     * @param userPasswordUpdateRequestDto userPasswordUpdateRequestDto
     * @return success/failure response
     */
    @Secured({USER_ROLE, MERCHANT_ROLE})
    @PutMapping(path = "/password", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> updatePassword(@RequestBody UserPasswordUpdateRequestDto
                                                                  userPasswordUpdateRequestDto,
                                                          @RequestHeader(value = HEADER_USER_ID) String userId) {
        try {
            if (userPasswordUpdateRequestDto.isRequiredAvailable()) {
                if (isValidPasswordForUpdate(userPasswordUpdateRequestDto)) {
                    return updatePasswordIfValid(userPasswordUpdateRequestDto);
                } else {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_PASSWORD);
                }
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (AuthServiceException e) {
            log.error("Updating password was failed for userId: {}", userId, e);
            return getInternalServerError();
        }

    }

    /**
     * This method is used to get OTP if user forgets password.
     *
     * @param mobileNoRequestDto forgotPasswordMobileNumberRequestDto
     * @return success/failure response
     */
    @PostMapping(path = "/forgot-password", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> forgetPassword(
            @RequestBody ForgotPasswordRequestDto mobileNoRequestDto) {
        try {
            if (mobileNoRequestDto.isRequiredAvailable()) {
                if (validator.isValidMobileNoWithCountryCode(mobileNoRequestDto.getMobileNo().getNo())) {
                    userService.sendPasswordResetToken(mobileNoRequestDto.getMobileNo());
                    return getSuccessResponse(SuccessResponseStatusType.PASSWORD_RESET_TOKEN, null);
                } else {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_MOBILE_NO);
                }
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (SendSmsFailedException e) {
            log.error("Sending password reset token to : {} was failed.", mobileNoRequestDto.getMobileNo().getNo(), e);
            return getBadRequestError(ErrorResponseStatusType.FAILED_TO_SEND_PW_RESET_TOKEN);
        } catch (InvalidUserException e) {
            log.error("No Registered user found for the mobileNo: {}: ", mobileNoRequestDto.getMobileNo().getNo(), e);
            return getBadRequestError(ErrorResponseStatusType.NO_USER);
        } catch (AuthServiceException e) {
            log.error("Sending forgot password reset token was failed for mobileNo {}: ", mobileNoRequestDto.getMobileNo().getNo(), e);
            return getInternalServerError();
        }
    }

    /**
     * This method resets the current password of the user
     *
     * @param resetPasswordRequestDto resetPasswordRequestDto
     * @return success/failure response
     */
    @PutMapping(path = "/password/reset", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        try {
            if (resetPasswordRequestDto.isRequiredAvailable()) {
                if (validator.isValidPassword(resetPasswordRequestDto.getPassword())) {
                    return resetPasswordForValidInputs(resetPasswordRequestDto);
                } else {
                    return getBadRequestError(ErrorResponseStatusType.INVALID_PASSWORD);
                }
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (InvalidUserException e) {
            log.error("No Registered user found ", e);
            return getBadRequestError(ErrorResponseStatusType.NO_USER);
        } catch (ExpiredPasswordResetTokenException e) {
            log.error("Reset password token was expired ", e);
            return getBadRequestError(ErrorResponseStatusType.PASSWORD_RESET_TOKEN_WAS_EXPIRED);
        } catch (AuthServiceException e) {
            log.error("Resetting password was failed for userId: ", e);
            return getInternalServerError();
        }
    }

    /**
     * This method returns bulk user info
     *
     * @param bulkUserRequestDto bulkUserRequestDto
     * @return bulk user info
     */
    @PostMapping(value = "/bulk-info", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> getBulkUsers(@RequestBody BulkUserRequestDto bulkUserRequestDto,
                                                        @RequestHeader(name = TIME_ZONE_HEADER) String timeZone) {
        try {
            if (!isValidTimeZone(timeZone)) {
                log.debug("Invalid timeZone: {}", timeZone);
                return getBadRequestError(ErrorResponseStatusType.INVALID_TIMEZONE);
            }
            if (bulkUserRequestDto.isRequiredAvailable()) {
                if (bulkUserRequestDto.getUserIds().size() > MAX_USER_COUNT) {
                    return getBadRequestError(ErrorResponseStatusType.MAX_USER_REQUEST_COUNT);
                }
                List<BasicUserResponseDto> userList = userService.getUserList(bulkUserRequestDto.getUserIds(), timeZone);
                return getSuccessResponse(SuccessResponseStatusType.GET_USERS, new BulkUserResponseDto(userList));
            } else {
                return getBadRequestError(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
            }
        } catch (AuthServiceException e) {
            log.error("Getting user list failed ", e);
            return getInternalServerError();
        }
    }

    /**
     * Reset password the user token will be revoked
     *
     * @param resetPasswordRequestDto resetPasswordRequestDto
     * @return success/failure response
     */
    private ResponseEntity<ResponseWrapper> resetPasswordForValidInputs(ResetPasswordRequestDto
                                                                                resetPasswordRequestDto) {
        User user = userService.resetPassword(resetPasswordRequestDto);
        Collection<OAuth2AccessToken> accessTokens = userService.getTokenByUserId(user.getId());
        for (OAuth2AccessToken token : new ArrayList<>(accessTokens)) {
            if (token != null) {
                defaultTokenServices.revokeToken(token.getValue());
            }
        }
        return getSuccessResponse(SuccessResponseStatusType.RESET_PASSWORD, null);
    }

    /**
     * Returns the update numbers list
     *
     * @param page page
     * @param size size
     * @return updated mobile numbers
     */
    @GetMapping(path = "/mobile-no/updates/{page}/{size}", consumes = APPLICATION_JSON_UTF_8, produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> getUpdatedMobileNumbers(@Min(DEFAULT_PAGE) @PathVariable("page") Integer page,
                                                                   @Min(DEFAULT_PAGE) @Max(PAGE_MAX_SIZE)
                                                                   @Positive @PathVariable("size") Integer size) {
        try {
            log.debug("Getting updated numbers page: {}, size: {}", page, size);
            Pageable pageable = PageRequest.of(page, size);
            UpdatedMobileNoListResponseDto updateMobileNoList = userService.getUpdateMobileNoList(pageable);
            return getSuccessResponse(SuccessResponseStatusType.GET_USER_UPDATED_MOBILE_NO, updateMobileNoList);
        } catch (AuthServiceException e) {
            log.error("Getting updated mobile numbers failed ", e);
            return getInternalServerError();
        }
    }


    /**
     * This method is used to return a list of mobile users & merchant.
     * Supported roles: User & Merchant
     * If searchTerm is ALL then all users will be returned.
     *
     * @param userId     userId
     * @param timeZone   timeZone
     * @param type       RoleType
     * @param page       page
     * @param size       size
     * @param searchTerm searchTerm
     * @return user list
     */
    @Secured({ADMIN_ROLE})
    @GetMapping(path = "/{type}/{page}/{size}/search/{searchTerm}", consumes = APPLICATION_JSON_UTF_8,
            produces = APPLICATION_JSON_UTF_8)
    public ResponseEntity<ResponseWrapper> listAllUser(@RequestHeader(name = HEADER_USER_ID) String userId,
                                                       @RequestHeader(name = TIME_ZONE_HEADER) String timeZone,
                                                       @PathVariable RoleType type,
                                                       @Min(DEFAULT_PAGE) @PathVariable("page") Integer page,
                                                       @Min(DEFAULT_PAGE) @Max(PAGE_MAX_SIZE)
                                                       @Positive @PathVariable("size") Integer size,
                                                       @PathVariable("searchTerm") String searchTerm) {
        try {
            if (type == RoleType.ADMIN) {
                return getBadRequestError(ErrorResponseStatusType.UNSUPPORTED_USER_TYPE);
            }
            if (!isValidTimeZone(timeZone)) {
                log.debug("Invalid time zone for userId: {}, Time zone: {}", userId, timeZone);
                return getBadRequestError(ErrorResponseStatusType.INVALID_TIMEZONE);
            }
            Pageable pageable = PageRequest.of(page, size);
            if (type == RoleType.MERCHANT || type == RoleType.BANK) {
                Page<MerchantBankSearchIndex> merchantPage =
                        userRoleService.getMerchantsOrBanks(searchTerm, pageable, type);
                log.debug("Successfully returned merchant list for userId: {}. RoleType: {}, Page: {}, Size: {}, " +
                        "SearchTerm: {}", userId, type.name(), page, size, searchTerm);
                return getSuccessResponse(SuccessResponseStatusType.GET_USERS,
                        new AllMerchantListResponseDto(merchantPage, timeZone, type));
            } else {
                Page<User> userPage = userRoleService.getUsers(type.getId(), searchTerm, pageable);
                log.debug("Successfully returned users list for userId: {}. RoleType: {}, Page: {}, Size: {}, " +
                        "SearchTerm: {}", userId, type.name(), page, size, searchTerm);
                return getSuccessResponse(SuccessResponseStatusType.GET_USERS,
                        new AllUserListResponseDto(userPage, timeZone));
            }
        } catch (AuthServiceException e) {
            log.error("Getting users list failed for userId: {}. RoleType: {}, Page: {}, Size: {}, SearchTerm: {}",
                    userId, type.name(), page, size, searchTerm, e);
            return getInternalServerError();
        }
    }

    /**
     * Create a unregistered user
     *
     * @param unregisterUserDto unregisterUserDto
     * @param user              user
     * @return response
     */
    private ResponseEntity<ResponseWrapper> createUnregisteredUserIfValidUser(UnregisterUserDto unregisterUserDto,
                                                                              User user, String timeZone) {
        try {
            userService.createUnregisteredUser(user);
            UserResponseDto userResponseDto = new UserResponseDto(user, timeZone);
            log.debug("Created user: {} with {}",
                    user.getId(), userResponseDto.toLogJson());
            return getSuccessResponse(SuccessResponseStatusType.CREATE_USER, userResponseDto);
        } catch (MobileNoAlreadyExistsException e) {
            log.error(ALREADY_EXISTING_MOBILE_NO, unregisterUserDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.EXISTING_MOBILE_NO);
        }
    }

    /**
     * Update the password of a valid user
     *
     * @param userPasswordUpdateRequestDto userPasswordUpdateRequestDto
     * @return response
     */
    private ResponseEntity<ResponseWrapper> updatePasswordIfValid(UserPasswordUpdateRequestDto
                                                                          userPasswordUpdateRequestDto) {
        try {
            userService.updatePassword(userPasswordUpdateRequestDto);
            revokeToken();
            return getSuccessResponse(SuccessResponseStatusType.UPDATE_PASSWORD, null);
        } catch (InvalidUserException e) {
            log.error(GETTING_USERS_FOR_INVALID_USER_ID, userPasswordUpdateRequestDto.getUserId(), e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_USER_ID);
        } catch (InvalidPasswordException e) {
            log.error("Password does not matched for userId {} with request {} ",
                    userPasswordUpdateRequestDto.getUserId(), userPasswordUpdateRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.OLD_PASSWORD_DOES_NOT_MATCH);
        }
    }

    /**
     * Check for valid input password
     *
     * @param userPasswordUpdateRequestDto userPasswordUpdateRequestDto
     * @return true/false
     */
    private boolean isValidPasswordForUpdate(UserPasswordUpdateRequestDto userPasswordUpdateRequestDto) {
        return validator.isValidPassword(userPasswordUpdateRequestDto.getPassword()) &&
                validator.isValidPassword(userPasswordUpdateRequestDto.getNewPassword());
    }

    /**
     * Update mobile No of a valid user
     *
     * @param userMobileUpdateRequestDto userMobileUpdateRequestDto
     * @return userDetailResponseDto
     */
    private ResponseEntity<ResponseWrapper> updateMobileForValidUser(UserMobileUpdateRequestDto
                                                                             userMobileUpdateRequestDto) {
        try {
            User user = userService.updateMobileNo(userMobileUpdateRequestDto);
            UserMobileNo userMobileNo = new UserMobileNo(userMobileUpdateRequestDto.getMobileNo().getNo(),
                    userMobileUpdateRequestDto.getUserId());
            userService.addUpdatedNumber(userMobileNo);
            revokeToken();
            UserDetailResponseDto userDetailResponseDto = new UserDetailResponseDto(user);
            return getSuccessResponse(SuccessResponseStatusType.UPDATE_MOBILE_NO, userDetailResponseDto);
        } catch (InvalidUserException e) {
            log.error(GETTING_USERS_FOR_INVALID_USER_ID, userMobileUpdateRequestDto.getUserId(), e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_USER_ID);
        } catch (MobileNoAlreadyExistsException e) {
            log.error(ALREADY_EXISTING_MOBILE_NO, userMobileUpdateRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.EXISTING_MOBILE_NO);
        }
    }

    /**
     * update email of a valid user
     *
     * @param emailUpdateRequestDto emailUpdateRequestDto
     * @return userDetailResponseDto
     */
    private ResponseEntity<ResponseWrapper> updateEmailForValidUser(UserEmailUpdateRequestDto emailUpdateRequestDto) {
        try {
            User user;
            if (userService.isFirstTimeUserEmail(emailUpdateRequestDto.getUserId())) {
                user = userService.updateEmail(emailUpdateRequestDto);
            } else {
                user = userService.updateEmail(emailUpdateRequestDto);
                revokeToken();
            }
            UserDetailResponseDto userDetailResponseDto = new UserDetailResponseDto(user);
            log.debug("Updated email for {}: {}", user.getId(), userDetailResponseDto.toLogJson());
            return getSuccessResponse(SuccessResponseStatusType.UPDATE_EMAIL, userDetailResponseDto);
        } catch (InvalidUserException e) {
            log.error(GETTING_USERS_FOR_INVALID_USER_ID, emailUpdateRequestDto.getUserId(), e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_USER_ID);
        } catch (EmailAlreadyExistsException e) {
            log.error("Already existing email: {}", emailUpdateRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.EXISTING_EMAIL);
        }
    }

    /**
     * This method fetch user from service fot given mobile No
     *
     * @param mobileNo mobileNo
     * @return user
     */
    private ResponseEntity<ResponseWrapper> fetchUserByMobileNo(String mobileNo) {
        try {
            User user = userService.getUserByMobileNo(mobileNo);
            UserDetailByMobileResponseDto userDetailResponseDto = new UserDetailByMobileResponseDto(user);
            log.debug("Return user for {}: {}", user.getId(), userDetailResponseDto.toLogJson());
            return getSuccessResponse(SuccessResponseStatusType.GET_USER, userDetailResponseDto);
        } catch (InvalidUserException e) {
            log.error("Getting users fot invalid mobileNo" +
                    ": {}", mobileNo, e);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * This method fetch user from service for given user Id
     *
     * @param userId userId
     * @return user
     */
    private ResponseEntity<ResponseWrapper> fetchUserById(String userId) {
        try {
            User user = userService.getUserByUserId(userId);
            UserDetailResponseDto userDetailResponseDto = new UserDetailResponseDto(user);
            log.debug("Return user for {}: {}", userId, userDetailResponseDto.toLogJson());
            return getSuccessResponse(SuccessResponseStatusType.GET_USER, userDetailResponseDto);
        } catch (InvalidUserException e) {
            log.error("Getting users fot invalid userId. userId: {}", userId, e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_USER_ID);
        }
    }

    /**
     * This method creates a user with valid inputs.
     *
     * @param user user
     * @return userId and language
     */
    private ResponseEntity<ResponseWrapper> createUser(User user, String timeZone) {
        if (!userService.isAlreadyRegisteredUser(user.getMobileNo())) {
            if (user.getEmail() != null && userService.isEmailExist(user.getEmail())) {
                return getBadRequestError(ErrorResponseStatusType.EXISTING_EMAIL);
            } else {
                if (userService.isMobileNoExist(user.getMobileNo())) {
                    User existingUser = userService.getUserByMobileNo(user.getMobileNo());
                    user.setId(existingUser.getId());
                    user.setCreatedAt(existingUser.getCreatedAt());
                }
                userService.createUser(user);
                UserResponseDto userResponseDto = new UserResponseDto(user, timeZone);
                log.debug("Created user: {} with {}",
                        user.getId(), userResponseDto.toLogJson());
                return getSuccessResponse(SuccessResponseStatusType.CREATE_USER, userResponseDto);
            }

        } else {
            return getBadRequestError(getCreateUserExistingFields(user));
        }
    }

    /**
     * This method updates a valid user
     *
     * @param userUpdateRequestDto userUpdateRequestDto
     * @return userDetailResponseDto
     */
    private ResponseEntity<ResponseWrapper> updateIfValidUser(UserUpdateRequestDto userUpdateRequestDto) {
        try {
            User user = userService.updateUser(userUpdateRequestDto);
            UserDetailResponseDto userDetailResponseDto = new UserDetailResponseDto(user);
            log.debug("Updated user: {} with {}",
                    user.getId(), userDetailResponseDto.toLogJson());
            return getSuccessResponse(SuccessResponseStatusType.UPDATE_USER, userDetailResponseDto);
        } catch (InvalidUserException e) {
            log.error("Getting user for invalid userId. userId: {}", userUpdateRequestDto.toLogJson(), e);
            return getBadRequestError(ErrorResponseStatusType.INVALID_USER_ID);
        }
    }

    /**
     * This method checks the validity of mobile no, email and password.
     *
     * @param userRequestDto userRequestDto
     * @return true/ false
     */
    private boolean isValidInput(UserRequestDto userRequestDto) {
        if (userRequestDto.getEmail() != null) {
            return validator.isValidMobileNoWithCountryCode(userRequestDto.getMobileNo().getNo())
                    && validator.isValidEmail(userRequestDto.getEmail())
                    && validator.isValidPassword(userRequestDto.getPassword())
                    && Language.isValidLanguage(userRequestDto.getLanguage());
        }
        return validator.isValidMobileNoWithCountryCode(userRequestDto.getMobileNo().getNo())
                && validator.isValidPassword(userRequestDto.getPassword())
                && Language.isValidLanguage(userRequestDto.getLanguage());
    }

    /**
     * Get invalid fields for create user
     *
     * @param userRequestDto userRequestDto
     * @return errorResponseStatusType
     */
    private ErrorResponseStatusType getCreateUserInvalidFields(UserRequestDto userRequestDto) {
        if (!validator.isValidMobileNoWithCountryCode(userRequestDto.getMobileNo().getNo())) {
            return ErrorResponseStatusType.INVALID_MOBILE_NO;
        }
        if (userRequestDto.getEmail() != null &&
                !validator.isValidEmail(userRequestDto.getEmail())) {
            return ErrorResponseStatusType.INVALID_EMAIL;
        }
        if (!validator.isValidPassword(userRequestDto.getPassword())) {
            return ErrorResponseStatusType.INVALID_PASSWORD;
        }
        if (!Language.isValidLanguage(userRequestDto.getLanguage())) {
            return ErrorResponseStatusType.INVALID_LANGUAGE;
        }
        return ErrorResponseStatusType.INTERNAL_SERVER_ERROR;
    }

    /**
     * This method creates list of existing fields for user.
     *
     * @param user user
     * @return list of existing fields
     */
    private ErrorResponseStatusType getCreateUserExistingFields(User user) {

        if (userService.isMobileNoExist(user.getMobileNo())) {
            return ErrorResponseStatusType.EXISTING_MOBILE_NO;
        }
        if (user.getEmail() != null && userService.isEmailExist(user.getEmail())) {
            return ErrorResponseStatusType.EXISTING_EMAIL;
        }
        return ErrorResponseStatusType.INTERNAL_SERVER_ERROR;
    }


    /**
     * This method revokes the user token
     */
    private void revokeToken() {
        final OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder
                .getContext().getAuthentication();
        final String userToken = defaultTokenServices.getAccessToken(authentication).getValue();
        try {
            defaultTokenServices.revokeToken(userToken);
        } catch (DataAccessException e) {
            log.error(TOKEN_REVOCATION_ERROR, e);
            throw new AuthServiceException(TOKEN_REVOCATION_ERROR);
        }
    }

    /**
     * This method checks the given image url is invalid for when the imageUrl is optional.
     *
     * @param imageUrl imageUrl
     * @return true/ false
     */
    private boolean isInvalidForOptionalImageUrl(String imageUrl) {
        return imageUrl != null && !imageUrl.trim().isEmpty() && !validator.isValidUrl(imageUrl.trim());
    }

    /**
     * This method checks the given language is invalid.
     *
     * @param language language
     * @return true/ false
     */
    private boolean isInvalidForOptionalLanguage(String language) {
        return language != null && !Language.isValidLanguage(language);
    }

}
