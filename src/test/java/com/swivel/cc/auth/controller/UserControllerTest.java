//package com.swivel.cc.auth.controller;
//
//import com.swivel.cc.auth.configuration.ResourceBundleMessageSourceBean;
//import com.swivel.cc.auth.configuration.Translator;
//import com.swivel.cc.auth.domain.entity.User;
//import com.swivel.cc.auth.domain.entity.UserMobileNo;
//import com.swivel.cc.auth.domain.request.*;
//import com.swivel.cc.auth.domain.response.BasicUserResponseDto;
//import com.swivel.cc.auth.domain.response.BulkUserResponseDto;
//import com.swivel.cc.auth.domain.response.UpdatedMobileNoListResponseDto;
//import com.swivel.cc.auth.domain.response.UpdatedMobileNoResponseDto;
//import com.swivel.cc.auth.enums.ErrorResponseStatusType;
//import com.swivel.cc.auth.enums.Language;
//import com.swivel.cc.auth.enums.SuccessResponseStatusType;
//import com.swivel.cc.auth.exception.*;
//import com.swivel.cc.auth.service.UserService;
//import com.swivel.cc.auth.util.Validator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.TokenRequest;
//import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
//import org.springframework.security.oauth2.provider.client.BaseClientDetails;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.*;
//
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.startsWith;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.mockito.MockitoAnnotations.initMocks;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * This class tests the {@link UserController} class.
// */
//class UserControllerTest {
//
//    public static final String X_USER_ID = "User-Id";
//    private static final String USER_NAME = "Rendy Deraman";
//    private static final String USER_ID = "uid-d9d069f9-2b07-446f-8117-e78ff14d01d8";
//    private static final String USER_EMAIL = "rengy@tokoin.io";
//    private static final String USER_MOBILE_NO = "+62-81123123";
//    private static final String COUNTRY_CODE = "+62";
//    private static final String LOCAL_NUMBER = "81123123";
//    private static final String DISPLAY_NUMBER = "+6281123123";
//    private static final String USER_PASS = "Tokoin@123";
//    private static final String USER_LANG = "Bahasa";
//    private static final String CREATE_USER_URI = "/api/v1/users";
//    private static final String UPDATE_USER_URI = "/api/v1/users";
//    private static final String GET_USER_BY_ID_URI = "/api/v1/users";
//    private static final String GET_USER_BY_MOBILE_URI = "/api/v1/users/mobileNo/{mobileNo}";
//    private static final String UPDATE_USER_MOBILE_NO_URI = "/api/v1/users/mobileNo";
//    private static final String CREATE_UNREGISTERED_USER_URI = "/api/v1/users/mobileNo";
//    private static final String UPDATE_USER_EMAIL_URI = "/api/v1/users/email";
//    private static final String UPDATE_USER_PASSWORD_URI = "/api/v1/users/password";
//    private static final String RESET_USER_PASSWORD_URI = "/api/v1/users/password/reset";
//    private static final String GET_UPDATED_NUMBERS_URI = "/api/v1/users/mobile-no/updates/0/250";
//    private static final String GET_BULK_USERS = "/api/v1/users/bulk-info";
//    private static final String ERROR_STATUS = "ERROR";
//    private static final String APPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";
//    private static final String APPLICATION_JSON = "application/json";
//    private static final String MISSING_REQUIRED_FIELDS = "Required fields are missing";
//    private static final String SUCCESS_MESSAGE = "Successfully created the user";
//    private static final String SUCCESS = "SUCCESS";
//    private static final String IMAGE_URL = "https://tokobook.s3.ap-southeast-1.amazonaws.com/test";
//    private final Pageable pageable = PageRequest.of(0, 200);
//    private final UserRequestDto userRequestDto = getSampleUserRequestDto();
//    private final UnregisterUserDto unregisterUserDto = getUnregisterUserDto();
//    private final UserEmailUpdateRequestDto userEmailUpdateRequestDto = getSampleEmailUpdateRequestDto();
//    private final UserMobileUpdateRequestDto userMobileUpdateRequestDto = getSampleMobileUpdateRequestDto();
//    private final UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = getSamplePasswordUpdateRequestDto();
//    private final ResetPasswordRequestDto resetPasswordRequestDto = getSampleResetPasswordRequestDto();
//    private final UserUpdateRequestDto userUpdateRequestDto = getSampleUserUpdateRequestDto();
//    private final User user = getSampleUser();
//    private final ResourceBundleMessageSourceBean resourceBundleMessageSourceBean = new ResourceBundleMessageSourceBean();
//    private final UserMobileNo userMobileNo = getUserMobileNo();
//    Locale locale = LocaleContextHolder.getLocale();
//    @Mock
//    private UserService userService;
//    @Mock
//    private Validator validator;
//
//    private MockMvc mockMvc;
//
//    private OAuth2Authentication authentication;
//    @Mock
//    private DefaultTokenServices defaultTokenServices;
//    @Mock
//    private TokenStore tokenStore;
//    @Mock
//    private OAuth2AuthenticationDetails details1;
//    @Mock
//    private OAuth2AccessToken oAuth2AccessToken;
//    @Mock
//    private Translator translator;
//    private ResourceBundleMessageSource resourceBundleMessageSource;
//
//    @BeforeEach
//    void setUp() {
//        initMocks(this);
//        when(validator.isValidPassword(USER_PASS)).thenReturn(true);
//        when(validator.isValidMobileNoWithCountryCode(USER_MOBILE_NO)).thenReturn(true);
//        when(validator.isValidEmail(USER_EMAIL)).thenReturn(true);
//        UserController userController = new UserController(userService, validator, defaultTokenServices, translator);
//
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//
//        final ClientDetails client = new BaseClientDetails("client", null, "read",
//                "password", "ROLE_CLIENT");
//        final OAuth2AccessToken token = new DefaultOAuth2AccessToken("FOO");
//        authentication = new OAuth2Authentication(
//                new TokenRequest(null, "client", null,
//                        "password").createOAuth2Request(client), null);
//        authentication.setDetails(details1);
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        tokenStore.storeAccessToken(token, authentication);
//        this.resourceBundleMessageSource = resourceBundleMessageSourceBean.messageSource();
//    }
//
//    /*
//     * Start of tests for create a user
//     * Api context : /api/v1/users/
//     */
//
//    /*
//     * Req (mobileNo -> 1, email -> 0)
//     * 1. DB (mobileNo -> 0) -> (save -> 1)
//     * 2. DB (mobileNo -> 1, reg -> 1) -> (save -> 0)
//     * 3. DB (mobileNo -> 1,  reg -> 0) -> (save -> 1)
//     */
//
//    /*
//     * Req (mobileNo -> 1, email -> 1)
//     * 4. DB (mobileNo -> 0, email -> 0) -> (save -> 1)
//     * 5. DB (mobileNo -> 0, email -> 1) -> (save -> 0)
//     * 6. DB (mobileNo -> 1, email -> 0, reg -> 1) -> (save -> 0)
//     * 7. DB (mobileNo -> 1, email -> 1, reg -> 1) -> (save -> 0)
//     * 8. DB (mobileNo -> 1, email -> 0, reg -> 0) -> (save -> 1)
//     * 9. DB (mobileNo -> 1, email -> 1, reg -> 0) -> (save -> 0)
//     */
//
//    /**
//     * Req (mobileNo -> 1, email -> 0)
//     * 1. DB (mobileNo -> 0) -> (save -> 1)
//     */
//    @Test
//    void Should_ReturnOk_When_CreatingFreshUserWithoutProvidingEmail() throws Exception {
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        userRequestDto.setEmail(null);
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isAlreadyRegisteredUser(mobileNo)).thenReturn(false);
//        when(userService.isEmailExist(null)).thenThrow(new AuthServiceException("failed"));
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status", is("SUCCESS")))
//                .andExpect(jsonPath("$.message", is(SUCCESS_MESSAGE)))
//                .andExpect(jsonPath("$.data.userId", startsWith("uid-")))
//                .andExpect(jsonPath("$.data.language").value("BAHASA"));
//    }
//
//
//    /**
//     * Req (mobileNo -> 1, email -> 0)
//     * 2. DB (mobileNo -> 1, reg -> 1) -> (save -> 0)
//     */
//    @Test
//    void Should_ReturnBadRequest_When_UserIsAlreadyExistingForCreateUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .EXISTING_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .EXISTING_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.EXISTING_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        userRequestDto.setEmail(null);
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isMobileNoExist(mobileNo)).thenReturn(true);
//        when(userService.isAlreadyRegisteredUser(mobileNo)).thenReturn(true);
//        when(userService.isEmailExist(null)).thenThrow(new AuthServiceException("failed"));
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.EXISTING_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Req (mobileNo -> 1, email -> 0)
//     * 3. DB (mobileNo -> 1,  reg -> 0) -> (save -> 1)
//     */
//    @Test
//    void Should_ReturnOk_When_CreatingUserIsSuccessfulForUnregisteredUser() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.CREATE_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.CREATE_USER.getCode()))
//                .thenReturn(successMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        userRequestDto.setEmail(null);
//        when(userService.isAlreadyRegisteredUser(mobileNo)).thenReturn(false);
//        when(userService.isMobileNoExist(mobileNo)).thenReturn(true);
//        when(userService.isEmailExist(null)).thenThrow(new AuthServiceException("failed"));
//        User user = new User();
//        user.setId(USER_ID);
//        when(userService.getUserByMobileNo(mobileNo)).thenReturn(user);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status", is("SUCCESS")))
//                .andExpect(jsonPath("$.message", is(SUCCESS_MESSAGE)))
//                .andExpect(jsonPath("$.data.userId", startsWith("uid-")))
//                .andExpect(jsonPath("$.data.language").value("BAHASA"))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage));
//    }
//
//    /**
//     * Req (mobileNo -> 1, email -> 1)
//     * 4. DB (mobileNo -> 0, email -> 0) -> (save -> 1)
//     */
//    @Test
//    void Should_ReturnOk_When_CreatingFreshUserWithMobileNoAndEmail() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.CREATE_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.CREATE_USER.getCode()))
//                .thenReturn(successMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isAlreadyRegisteredUser(USER_MOBILE_NO)).thenReturn(false);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status", is("SUCCESS")))
//                .andExpect(jsonPath("$.message", is(SUCCESS_MESSAGE)))
//                .andExpect(jsonPath("$.data.userId", startsWith("uid-")))
//                .andExpect(jsonPath("$.data.language").value("BAHASA"))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage));
//    }
//
//
//    /**
//     * Req (mobileNo -> 1, email -> 1)
//     * 5. DB (mobileNo -> 0, email -> 1) -> (save -> 0)
//     */
//    @Test
//    void Should_ReturnBadRequest_When_EmailIsAlreadyExistingForFreshUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .EXISTING_EMAIL.getCodeString(ErrorResponseStatusType
//                                .EXISTING_EMAIL.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.EXISTING_EMAIL
//                .getCodeString(ErrorResponseStatusType.EXISTING_EMAIL.getCode())))
//                .thenReturn(invalidMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isMobileNoExist(mobileNo)).thenReturn(false);
//        when(userService.isEmailExist(USER_EMAIL)).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.EXISTING_EMAIL.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.EXISTING_EMAIL.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Req (mobileNo -> 1, email -> 1)
//     * 6. DB (mobileNo -> 1, email -> 0, reg -> 1) -> (save -> 0)
//     */
//    @Test
//    void Should_ReturnBadRequest_When_CreatingUserForAlreadyExistingMobileNoForRegisteredUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .EXISTING_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .EXISTING_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.EXISTING_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isAlreadyRegisteredUser(mobileNo)).thenReturn(true);
//        when(userService.isMobileNoExist(mobileNo)).thenReturn(true);
//        when(userService.isEmailExist(USER_EMAIL)).thenReturn(false);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status", is("ERROR")))
//                .andExpect(jsonPath("$.message", is(ErrorResponseStatusType.EXISTING_MOBILE_NO.getMessage())))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Req (mobileNo -> 1, email -> 1)
//     * 7. DB (mobileNo -> 1, email -> 1, reg -> 1) -> (save -> 0)
//     */
//    @Test
//    void Should_ReturnBadRequest_When_CreatingUserForAlreadyExistingMobileAndEmailForRegisteredUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .EXISTING_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .EXISTING_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.EXISTING_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isAlreadyRegisteredUser(mobileNo)).thenReturn(true);
//        when(userService.isMobileNoExist(mobileNo)).thenReturn(true);
//        when(userService.isEmailExist(USER_EMAIL)).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status", is("ERROR")))
//                .andExpect(jsonPath("$.message", is(ErrorResponseStatusType.EXISTING_MOBILE_NO.getMessage())))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Req (mobileNo -> 1, email -> 1)
//     * 8. DB (mobileNo -> 1, email -> 0, reg -> 0) -> (save -> 1)
//     */
//    @Test
//    void Should_ReturnOk_When_CreatingUserForUnregisteredUser() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.CREATE_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.CREATE_USER.getCode()))
//                .thenReturn(successMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isAlreadyRegisteredUser(mobileNo)).thenReturn(false);
//        when(userService.isMobileNoExist(mobileNo)).thenReturn(true);
//        when(userService.isEmailExist(mobileNo)).thenReturn(false);
//        User user = new User();
//        user.setId(USER_ID);
//        when(userService.getUserByMobileNo(mobileNo)).thenReturn(user);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status", is("SUCCESS")))
//                .andExpect(jsonPath("$.message", is(SuccessResponseStatusType.CREATE_USER.getMessage())))
//                .andExpect(jsonPath("$.data.userId", startsWith("uid-")))
//                .andExpect(jsonPath("$.data.language").value("BAHASA"))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage));
//    }
//
//    /**
//     * Req (mobileNo -> 1, email -> 1)
//     * 9. DB (mobileNo -> 1, email -> 1, reg -> 0) -> (save -> 0)
//     */
//    @Test
//    void Should_ReturnBadRequest_When_CreatingUserForUnregisteredUserForAlreadyExistingMobileNoAndEmail() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .EXISTING_EMAIL.getCodeString(ErrorResponseStatusType
//                                .EXISTING_EMAIL.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.EXISTING_EMAIL
//                .getCodeString(ErrorResponseStatusType.EXISTING_EMAIL.getCode())))
//                .thenReturn(invalidMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isAlreadyRegisteredUser(mobileNo)).thenReturn(false);
//        when(userService.isMobileNoExist(mobileNo)).thenReturn(true);
//        when(userService.isEmailExist(USER_EMAIL)).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status", is("ERROR")))
//                .andExpect(jsonPath("$.message", is(ErrorResponseStatusType.EXISTING_EMAIL.getMessage())))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.EXISTING_EMAIL.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_MobileNoIsUnavailable() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userRequestDto.setMobileNo(null);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(MISSING_REQUIRED_FIELDS))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_EmailNoIsInvalid() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_EMAIL.getCodeString(ErrorResponseStatusType
//                                .INVALID_EMAIL.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_EMAIL
//                .getCodeString(ErrorResponseStatusType.INVALID_EMAIL.getCode())))
//                .thenReturn(invalidMessage);
//        String mobileNo = userRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(validator.isValidEmail(USER_EMAIL)).thenReturn(false);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_EMAIL.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_EMAIL.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_MobileNoIsInvalid() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .INVALID_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.INVALID_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(USER_MOBILE_NO)).thenReturn(false);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_PasswordIsInvalid() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_PASSWORD.getCodeString(ErrorResponseStatusType
//                                .INVALID_PASSWORD.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_PASSWORD
//                .getCodeString(ErrorResponseStatusType.INVALID_PASSWORD.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(userRequestDto.getMobileNo().getNo())).thenReturn(true);
//        when(validator.isValidPassword(USER_PASS)).thenReturn(false);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_PASSWORD.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_PASSWORD.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_LanguageNoIsInvalid() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_LANGUAGE.getCodeString(ErrorResponseStatusType
//                                .INVALID_LANGUAGE.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_LANGUAGE
//                .getCodeString(ErrorResponseStatusType.INVALID_LANGUAGE.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(userRequestDto.getMobileNo().getNo())).thenReturn(true);
//        userRequestDto.setLanguage("test");
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_LANGUAGE.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_LANGUAGE.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnInternalServerError_When_UserCreationWasFailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new AuthServiceException("Failed")).when(userService).createUser(any(User.class));
//        when(validator.isValidMobileNoWithCountryCode(userRequestDto.getMobileNo().getNo())).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
//                .content(userRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Start of tests for create a user
//     * Api context : /api/v1/users/mobileNo
//     **/
//
//    @Test
//    void Should_ReturnOk_When_CreatingUnregisteredUserIsSuccessful() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.CREATE_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.CREATE_USER.getCode()))
//                .thenReturn(successMessage);
//        String mobileNo = unregisterUserDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.isAlreadyRegisteredUser(mobileNo)).thenReturn(false);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_UNREGISTERED_USER_URI)
//                .content(unregisterUserDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status", is("SUCCESS")))
//                .andExpect(jsonPath("$.message", is(SuccessResponseStatusType.CREATE_USER.getMessage())))
//                .andExpect(jsonPath("$.data.userId", startsWith("uid-")))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_MobileNoIsUnavailableForCreatingUnregisteredUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        unregisterUserDto.setMobileNo(null);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_UNREGISTERED_USER_URI)
//                .content(unregisterUserDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_MobileNoForCreatingUnregisteredUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .INVALID_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.INVALID_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(USER_MOBILE_NO)).thenReturn(false);
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_UNREGISTERED_USER_URI)
//                .content(unregisterUserDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_MobileNoIsAlreadyExistingCreatingUnregisteredUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .EXISTING_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .EXISTING_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.EXISTING_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        String mobileNo = unregisterUserDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        doThrow(new MobileNoAlreadyExistsException("Failed")).when(userService).createUnregisteredUser(any(User.class));
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_UNREGISTERED_USER_URI)
//                .content(unregisterUserDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.EXISTING_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnInternalServerError_When_CreateUnregisteredUserFailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(unregisterUserDto.getMobileNo().getNo())).thenReturn(true);
//        doThrow(new AuthServiceException("Failed")).when(userService).createUnregisteredUser(any(User.class));
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_UNREGISTERED_USER_URI)
//                .content(unregisterUserDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Start of tests for get a user by Id
//     * Api context : /api/v1/users/{userId}
//     **/
//    @Test
//    void Should_ReturnInternalServerError_When_GettingUserByIdFailed() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new AuthServiceException("Failed")).when(userService)
//                .getUserByUserId(USER_ID);
//        String uri = GET_USER_BY_ID_URI.replace("{userId}", USER_ID);
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ThrowException_When_GettingUserForInvalidUserId() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_USER_ID.getCodeString(ErrorResponseStatusType
//                                .INVALID_USER_ID.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_USER_ID
//                .getCodeString(ErrorResponseStatusType.INVALID_USER_ID.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new InvalidUserException("Failed")).when(userService)
//                .getUserByUserId(USER_ID);
//        String uri = GET_USER_BY_ID_URI.replace("{userId}", USER_ID);
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_USER_ID.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_USER_ID.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnOk_When_GettingUserIsSuccessful() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.GET_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.GET_USER.getCode()))
//                .thenReturn(successMessage);
//        User user = new User();
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        when(userService.getUserByUserId(USER_ID)).thenReturn(user);
//        String uri = GET_USER_BY_ID_URI.replace("{userId}", USER_ID);
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(SUCCESS))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.GET_USER.getMessage()))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage))
//                .andExpect(jsonPath("$.data.fullName", is("user1")))
//                .andExpect(jsonPath("$.data.email", is("user@gmail.com")))
//                .andExpect(jsonPath("$.data.mobileNo.countryCode", is(COUNTRY_CODE)))
//                .andExpect(jsonPath("$.data.mobileNo.localNumber", is(LOCAL_NUMBER)))
//                .andExpect(jsonPath("$.data.mobileNo.displayNumber", is(DISPLAY_NUMBER)))
//                .andExpect(jsonPath("$.data.imageUrl", is("")))
//                .andExpect(jsonPath("$.data.language", is("ENGLISH")))
//                .andExpect(jsonPath("$.data.registeredUser", is(true)));
//    }
//
//    /**
//     * Start of tests for get a user by mobileNo
//     * Api context : /api/v1/users/mobileNo/{mobileNo}
//     **/
//    @Test
//    void Should_ReturnInternalServerError_When_GettingUserByMobileFailed() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new AuthServiceException("Failed")).when(userService)
//                .getUserByMobileNo(USER_MOBILE_NO);
//        String uri = GET_USER_BY_MOBILE_URI.replace("{mobileNo}", USER_MOBILE_NO);
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_GettingUserByMobileNoForNonExistingMobileNo() throws Exception {
//        doThrow(new InvalidUserException(ErrorResponseStatusType.INVALID_MOBILE_NO.getMessage())).when(userService)
//                .getUserByMobileNo(USER_MOBILE_NO);
//        String uri = GET_USER_BY_MOBILE_URI.replace("{mobileNo}", USER_MOBILE_NO);
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_GettingUserByMobileNoForNonInvalidMobileNo() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .INVALID_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.INVALID_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(USER_MOBILE_NO)).thenReturn(false);
//        String uri = GET_USER_BY_MOBILE_URI.replace("{mobileNo}", USER_MOBILE_NO);
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnOk_When_GettingUserIsSuccessfulByMobileNo() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.GET_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.GET_USER.getCode()))
//                .thenReturn(successMessage);
//        User user = new User();
//        user.setId(USER_ID);
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        when(userService.getUserByMobileNo(USER_MOBILE_NO)).thenReturn(user);
//        String uri = GET_USER_BY_MOBILE_URI.replace("{mobileNo}", USER_MOBILE_NO);
//        mockMvc.perform(MockMvcRequestBuilders.get(uri)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(SUCCESS))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.GET_USER.getMessage()))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage))
//                .andExpect(jsonPath("$.data.userId", is(USER_ID)))
//                .andExpect(jsonPath("$.data.fullName", is("user1")))
//                .andExpect(jsonPath("$.data.email", is("user@gmail.com")))
//                .andExpect(jsonPath("$.data.mobileNo.countryCode", is(COUNTRY_CODE)))
//                .andExpect(jsonPath("$.data.mobileNo.localNumber", is(LOCAL_NUMBER)))
//                .andExpect(jsonPath("$.data.mobileNo.displayNumber", is(DISPLAY_NUMBER)))
//                .andExpect(jsonPath("$.data.imageUrl", is("")))
//                .andExpect(jsonPath("$.data.language", is("ENGLISH")))
//                .andExpect(jsonPath("$.data.registeredUser", is(true)));
//    }
//
//    /**
//     * Start of tests for updating user's mobile number
//     * Api context : /api/v1/users/mobileNo
//     **/
//
//    @Test
//    void Should_ReturnOk_When_UpdatingMobileNoIsSuccessful() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.UPDATE_MOBILE_NO.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.UPDATE_MOBILE_NO.getCode()))
//                .thenReturn(successMessage);
//        when(validator.isValidMobileNoWithCountryCode(userMobileUpdateRequestDto.getMobileNo().getNo())).thenReturn(true);
//        User user = new User();
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//        when(userService.updateMobileNo(any(UserMobileUpdateRequestDto.class), eq(USER_ID)))
//                .thenReturn(user);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_MOBILE_NO_URI)
//                .content(userMobileUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(SUCCESS))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.UPDATE_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage))
//                .andExpect(jsonPath("$.data.fullName", is("user1")))
//                .andExpect(jsonPath("$.data.email", is("user@gmail.com")))
//                .andExpect(jsonPath("$.data.mobileNo.countryCode", is(COUNTRY_CODE)))
//                .andExpect(jsonPath("$.data.mobileNo.localNumber", is(LOCAL_NUMBER)))
//                .andExpect(jsonPath("$.data.mobileNo.displayNumber", is(DISPLAY_NUMBER)))
//                .andExpect(jsonPath("$.data.imageUrl", is("")))
//                .andExpect(jsonPath("$.data.language", is("ENGLISH")))
//                .andExpect(jsonPath("$.data.registeredUser", is(true)));
//    }
//
//    @Test
//    void Should_Return_InternalServerError_When_TokenRevocationFailedForUpdateMobileNo() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(userMobileUpdateRequestDto.getMobileNo().getNo())).thenReturn(true);
//        User user = new User();
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo("+661323324");
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        when(userService.updateMobileNo(any(UserMobileUpdateRequestDto.class), eq(USER_ID)))
//                .thenReturn(user);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//        when(defaultTokenServices.revokeToken(oAuth2AccessToken.getValue())).thenThrow(new DataAccessException("Failed") {
//        });
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_MOBILE_NO_URI)
//                .content(userMobileUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_UpdateUserMobileNo_When_InvalidUserId_Provided() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_USER_ID.getCodeString(ErrorResponseStatusType
//                                .INVALID_USER_ID.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_USER_ID
//                .getCodeString(ErrorResponseStatusType.INVALID_USER_ID.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(userMobileUpdateRequestDto.getMobileNo().getNo())).thenReturn(true);
//        when(userService.updateMobileNo(any(UserMobileUpdateRequestDto.class), eq(USER_ID)))
//                .thenThrow(new InvalidUserException(("failed")));
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_MOBILE_NO_URI)
//                .content(userMobileUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_USER_ID.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_USER_ID.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnInternalServerError_When_UpdateMobileNo_FailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidMobileNoWithCountryCode(userMobileUpdateRequestDto.getMobileNo().getNo())).thenReturn(true);
//        when(userService.updateMobileNo(any(UserMobileUpdateRequestDto.class), eq(USER_ID)))
//                .thenThrow(new AuthServiceException(("failed")));
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_MOBILE_NO_URI)
//                .content(userMobileUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_MobileNo_Is_Null_For_UpdateMobileNo() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userMobileUpdateRequestDto.setMobileNo(null);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_MOBILE_NO_URI)
//                .content(userMobileUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_MobileNo_Is_Empty_For_UpdateMobileNo() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userMobileUpdateRequestDto.setMobileNo(null);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_MOBILE_NO_URI)
//                .content(userMobileUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_UpdateMobileNo_When_InvalidMobileNo_Provided() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .INVALID_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.INVALID_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
//        mobileNoRequestDto.setCountryCode("+23s");
//        mobileNoRequestDto.setLocalNumber("invalid");
//        userMobileUpdateRequestDto.setMobileNo(mobileNoRequestDto);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_MOBILE_NO_URI)
//                .content(userMobileUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_UpdateMobileNo_When_MobileNoIsAlreadyExists() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .EXISTING_MOBILE_NO.getCodeString(ErrorResponseStatusType
//                                .EXISTING_MOBILE_NO.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.EXISTING_MOBILE_NO
//                .getCodeString(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode())))
//                .thenReturn(invalidMessage);
//        String mobileNo = userMobileUpdateRequestDto.getMobileNo().getNo();
//        when(validator.isValidMobileNoWithCountryCode(mobileNo)).thenReturn(true);
//        when(userService.updateMobileNo(any(UserMobileUpdateRequestDto.class), eq(USER_ID)))
//                .thenThrow(new MobileNoAlreadyExistsException(("failed")));
//
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_MOBILE_NO_URI)
//                .content(userMobileUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.EXISTING_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.EXISTING_MOBILE_NO.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Start of tests for updating user's email address
//     * Api context : /api/v1/users/email
//     **/
//
//    @Test
//    void Should_ReturnOk_When_UpdatingEmailIsSuccessful() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.UPDATE_EMAIL.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.UPDATE_EMAIL.getCode()))
//                .thenReturn(successMessage);
//        User user = new User();
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        when(userService.updateEmail(any(UserEmailUpdateRequestDto.class), eq(USER_ID)))
//                .thenReturn(user);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_EMAIL_URI)
//                .content(userEmailUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(SUCCESS))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.UPDATE_EMAIL.getMessage()))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage))
//                .andExpect(jsonPath("$.data.fullName", is("user1")))
//                .andExpect(jsonPath("$.data.email", is("user@gmail.com")))
//                .andExpect(jsonPath("$.data.mobileNo.countryCode", is(COUNTRY_CODE)))
//                .andExpect(jsonPath("$.data.mobileNo.localNumber", is(LOCAL_NUMBER)))
//                .andExpect(jsonPath("$.data.mobileNo.displayNumber", is(DISPLAY_NUMBER)))
//                .andExpect(jsonPath("$.data.imageUrl", is("")))
//                .andExpect(jsonPath("$.data.language", is("ENGLISH")))
//                .andExpect(jsonPath("$.data.registeredUser", is(true)));
//    }
//
//    @Test
//    void Should_ReturnOk_When_UpdatingEmailIsSuccessfulForFirstTime() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.UPDATE_EMAIL.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.UPDATE_EMAIL.getCode()))
//                .thenReturn(successMessage);
//        User user = new User();
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        when(userService.isFirstTimeUserEmail(USER_ID)).thenReturn(true);
//        when(userService.updateEmail(any(UserEmailUpdateRequestDto.class), eq(USER_ID)))
//                .thenReturn(user);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_EMAIL_URI)
//                .content(userEmailUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(SUCCESS))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.UPDATE_EMAIL.getMessage()))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage))
//                .andExpect(jsonPath("$.data.fullName", is("user1")))
//                .andExpect(jsonPath("$.data.email", is("user@gmail.com")))
//                .andExpect(jsonPath("$.data.mobileNo.countryCode", is(COUNTRY_CODE)))
//                .andExpect(jsonPath("$.data.mobileNo.localNumber", is(LOCAL_NUMBER)))
//                .andExpect(jsonPath("$.data.mobileNo.displayNumber", is(DISPLAY_NUMBER)))
//                .andExpect(jsonPath("$.data.imageUrl", is("")))
//                .andExpect(jsonPath("$.data.language", is("ENGLISH")))
//                .andExpect(jsonPath("$.data.registeredUser", is(true)));
//    }
//
//
//    @Test
//    void Should_ReturnBadRequest_When_UpdateEmail_When_InvalidUserId_Provided() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_USER_ID.getCodeString(ErrorResponseStatusType
//                                .INVALID_USER_ID.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_USER_ID
//                .getCodeString(ErrorResponseStatusType.INVALID_USER_ID.getCode())))
//                .thenReturn(invalidMessage);
//        when(userService.updateEmail(any(UserEmailUpdateRequestDto.class), eq(USER_ID)))
//                .thenThrow(new InvalidUserException(("failed")));
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_EMAIL_URI)
//                .content(userEmailUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_USER_ID.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_USER_ID.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnInternalServerError_When_UpdateEmail_FailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        when(userService.updateEmail(any(UserEmailUpdateRequestDto.class), eq(USER_ID)))
//                .thenThrow(new AuthServiceException(("failed")));
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_EMAIL_URI)
//                .content(userEmailUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_Email_Is_Null_For_UpdateEmail() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userEmailUpdateRequestDto.setEmail(null);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_EMAIL_URI)
//                .content(userEmailUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_Email_Is_Empty_For_UpdateEmail() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userEmailUpdateRequestDto.setEmail("");
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_EMAIL_URI)
//                .content(userEmailUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_UpdateEmail_When_InvalidEmail_Provided() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_EMAIL.getCodeString(ErrorResponseStatusType
//                                .INVALID_EMAIL.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_EMAIL
//                .getCodeString(ErrorResponseStatusType.INVALID_EMAIL.getCode())))
//                .thenReturn(invalidMessage);
//        userEmailUpdateRequestDto.setEmail("invalid");
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_EMAIL_URI)
//                .content(userEmailUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_EMAIL.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_EMAIL.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_UpdateEmail_When_EmailIsAlreadyExists() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .EXISTING_EMAIL.getCodeString(ErrorResponseStatusType
//                                .EXISTING_EMAIL.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.EXISTING_EMAIL
//                .getCodeString(ErrorResponseStatusType.EXISTING_EMAIL.getCode())))
//                .thenReturn(invalidMessage);
//        when(userService.isMobileNoExist(USER_MOBILE_NO)).thenReturn(true);
//
//        when(userService.updateEmail(any(UserEmailUpdateRequestDto.class), eq(USER_ID)))
//                .thenThrow(new EmailAlreadyExistsException(("failed")));
//
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_EMAIL_URI)
//                .content(userEmailUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.EXISTING_EMAIL.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.EXISTING_EMAIL.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Start of tests for create update user
//     * Api context : put /api/v1/users
//     **/
//
//    @Test
//    void Should_ReturnOk_When_UpdatingUserIsSuccessful() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.UPDATE_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.UPDATE_USER.getCode()))
//                .thenReturn(successMessage);
//        User user = new User();
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//
//        when(userService.updateUser(any(UserUpdateRequestDto.class), eq(USER_ID)))
//                .thenReturn(user);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.message", is(SuccessResponseStatusType.UPDATE_USER.getMessage())))
//                .andExpect(jsonPath("$.displayMessage", is(successMessage)))
//                .andExpect(jsonPath("$.data.fullName", is("user1")))
//                .andExpect(jsonPath("$.data.email", is("user@gmail.com")))
//                .andExpect(jsonPath("$.data.mobileNo.countryCode", is(COUNTRY_CODE)))
//                .andExpect(jsonPath("$.data.mobileNo.localNumber", is(LOCAL_NUMBER)))
//                .andExpect(jsonPath("$.data.mobileNo.displayNumber", is(DISPLAY_NUMBER)))
//                .andExpect(jsonPath("$.data.imageUrl", is("")))
//                .andExpect(jsonPath("$.data.language", is("ENGLISH")))
//                .andExpect(jsonPath("$.data.registeredUser", is(true)));
//    }
//
//    @Test
//    void Should_ReturnOk_When_UpdatingUserIsSuccessful_With_Valid_Email() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.UPDATE_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.UPDATE_USER.getCode()))
//                .thenReturn(successMessage);
//        User user = new User();
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        when(userService.updateUser(any(UserUpdateRequestDto.class), eq(USER_ID)))
//                .thenReturn(user);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.message", is(SuccessResponseStatusType.UPDATE_USER.getMessage())))
//                .andExpect(jsonPath("$.displayMessage", is(successMessage)))
//                .andExpect(jsonPath("$.data.fullName", is("user1")))
//                .andExpect(jsonPath("$.data.email", is("user@gmail.com")))
//                .andExpect(jsonPath("$.data.mobileNo.countryCode", is(COUNTRY_CODE)))
//                .andExpect(jsonPath("$.data.mobileNo.localNumber", is(LOCAL_NUMBER)))
//                .andExpect(jsonPath("$.data.mobileNo.displayNumber", is(DISPLAY_NUMBER)))
//                .andExpect(jsonPath("$.data.imageUrl", is("")))
//                .andExpect(jsonPath("$.data.language", is("ENGLISH")))
//                .andExpect(jsonPath("$.data.registeredUser", is(true)));
//    }
//
//    @Test
//    void Should_ReturnOk_When_UpdatingUserIsSuccessful_With_Valid_EmailAndImageUrl() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.UPDATE_USER.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.UPDATE_USER.getCode()))
//                .thenReturn(successMessage);
//        User user = new User();
//        user.setFullName("user1");
//        user.setEmail("user@gmail.com");
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl(IMAGE_URL);
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        when(userService.updateUser(any(UserUpdateRequestDto.class), eq(USER_ID)))
//                .thenReturn(user);
//        userUpdateRequestDto.setImageUrl(IMAGE_URL);
//        when(validator.isValidUrl(IMAGE_URL)).thenReturn(true);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.message", is(SuccessResponseStatusType.UPDATE_USER.getMessage())))
//                .andExpect(jsonPath("$.displayMessage", is(successMessage)))
//                .andExpect(jsonPath("$.data.fullName", is("user1")))
//                .andExpect(jsonPath("$.data.email", is("user@gmail.com")))
//                .andExpect(jsonPath("$.data.mobileNo.countryCode", is(COUNTRY_CODE)))
//                .andExpect(jsonPath("$.data.mobileNo.localNumber", is(LOCAL_NUMBER)))
//                .andExpect(jsonPath("$.data.mobileNo.displayNumber", is(DISPLAY_NUMBER)))
//                .andExpect(jsonPath("$.data.imageUrl", is(IMAGE_URL)))
//                .andExpect(jsonPath("$.data.language", is("ENGLISH")))
//                .andExpect(jsonPath("$.data.registeredUser", is(true)));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_InvalidImageUrlProvidedToUpdateUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_IMAGE_URL.getCodeString(ErrorResponseStatusType
//                                .INVALID_IMAGE_URL.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_IMAGE_URL
//                .getCodeString(ErrorResponseStatusType.INVALID_IMAGE_URL.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidUrl("image_url")).thenReturn(false);
//        userUpdateRequestDto.setImageUrl("image_url");
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_IMAGE_URL.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_IMAGE_URL.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_InvalidLanguageIsProvidedToUpdateUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_LANGUAGE.getCodeString(ErrorResponseStatusType
//                                .INVALID_LANGUAGE.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_LANGUAGE
//                .getCodeString(ErrorResponseStatusType.INVALID_LANGUAGE.getCode())))
//                .thenReturn(invalidMessage);
//        userUpdateRequestDto.setLanguage("Latin");
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_LANGUAGE.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_LANGUAGE.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_UpdateUser_When_InvalidUserId_Provided() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_USER_ID.getCodeString(ErrorResponseStatusType
//                                .INVALID_USER_ID.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_USER_ID
//                .getCodeString(ErrorResponseStatusType.INVALID_USER_ID.getCode())))
//                .thenReturn(invalidMessage);
//        when(userService.updateUser(any(UserUpdateRequestDto.class), eq(USER_ID)))
//                .thenThrow(new InvalidUserException(("failed")));
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_USER_ID.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_USER_ID.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnInternalServerError_When_UpdateUser_FailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        when(userService.updateUser(any(UserUpdateRequestDto.class), eq(USER_ID)))
//                .thenThrow(new AuthServiceException(("failed")));
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_FullName_Is_Null_For_UpdateUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userUpdateRequestDto.setFullName(null);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//
//    @Test
//    void Should_ReturnBadRequest_When_FullName_Is_Empty_For_UpdateUser() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userUpdateRequestDto.setFullName("");
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_URI)
//                .content(userUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Start of tests for change password
//     * Api context : put /api/v1/users/password
//     **/
//
//    @Test
//    void Should_ReturnOk_When_UpdatingPasswordIsSuccessful() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.UPDATE_PASSWORD.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.UPDATE_PASSWORD.getCode()))
//                .thenReturn(successMessage);
//        doNothing().when(userService).updatePassword(any(UserPasswordUpdateRequestDto.class), eq(USER_ID));
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getPassword())).thenReturn(true);
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getNewPassword())).thenReturn(true);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(SUCCESS))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.UPDATE_PASSWORD.getMessage()))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage));
//    }
//
//    @Test
//    void Should_Return_InternalServerError_When_TokenRevocationFailedForUpdatePassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        doNothing().when(userService).updatePassword(any(UserPasswordUpdateRequestDto.class), eq(USER_ID));
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getPassword())).thenReturn(true);
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getNewPassword())).thenReturn(true);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//        when(defaultTokenServices.revokeToken(oAuth2AccessToken.getValue())).thenThrow(new DataAccessException("Failed") {
//        });
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header(X_USER_ID, USER_ID)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnInternalServerError_When_UpdatePassword_FailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new AuthServiceException("failed")).when(userService).
//                updatePassword(any(UserPasswordUpdateRequestDto.class), eq(USER_ID));
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getPassword())).thenReturn(true);
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getNewPassword())).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_UpdatePassword_When_InvalidUserId_Provided() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_USER_ID.getCodeString(ErrorResponseStatusType
//                                .INVALID_USER_ID.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_USER_ID
//                .getCodeString(ErrorResponseStatusType.INVALID_USER_ID.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new InvalidUserException("failed")).when(userService).
//                updatePassword(any(UserPasswordUpdateRequestDto.class), eq(USER_ID));
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getPassword())).thenReturn(true);
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getNewPassword())).thenReturn(true);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_USER_ID.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_USER_ID.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_Password_Is_Null_For_UpdatePassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userPasswordUpdateRequestDto.setPassword(null);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_Password_Is_Empty_For_UpdatePassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userPasswordUpdateRequestDto.setPassword("");
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_NewPassword_Is_Null_For_UpdatePassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userPasswordUpdateRequestDto.setNewPassword(null);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_NewPassword_Is_Empty_For_UpdatePassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        userPasswordUpdateRequestDto.setNewPassword("");
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_Input_Password_Is_Invalid_For_UpdatePassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_PASSWORD.getCodeString(ErrorResponseStatusType
//                                .INVALID_PASSWORD.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_PASSWORD
//                .getCodeString(ErrorResponseStatusType.INVALID_PASSWORD.getCode())))
//                .thenReturn(invalidMessage);
//        userPasswordUpdateRequestDto.setPassword("1234");
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getNewPassword())).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_PASSWORD.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_PASSWORD.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_Input_NewPassword_Is_Invalid_For_UpdatePassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_PASSWORD.getCodeString(ErrorResponseStatusType
//                                .INVALID_PASSWORD.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_PASSWORD
//                .getCodeString(ErrorResponseStatusType.INVALID_PASSWORD.getCode())))
//                .thenReturn(invalidMessage);
//        userPasswordUpdateRequestDto.setNewPassword("1234");
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getPassword())).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_PASSWORD.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_PASSWORD.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_CurrentPassword_Does_Not_Match() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .OLD_PASSWORD_DOES_NOT_MATCH.getCodeString(ErrorResponseStatusType
//                                .OLD_PASSWORD_DOES_NOT_MATCH.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.OLD_PASSWORD_DOES_NOT_MATCH
//                .getCodeString(ErrorResponseStatusType.OLD_PASSWORD_DOES_NOT_MATCH.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new InvalidPasswordException("failed")).when(userService).
//                updatePassword(any(UserPasswordUpdateRequestDto.class), eq(USER_ID));
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getPassword())).thenReturn(true);
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getNewPassword())).thenReturn(true);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.OLD_PASSWORD_DOES_NOT_MATCH.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.OLD_PASSWORD_DOES_NOT_MATCH.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_PasswordIsInvalidForUpdate_Password() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_PASSWORD.getCodeString(ErrorResponseStatusType
//                                .INVALID_PASSWORD.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_PASSWORD
//                .getCodeString(ErrorResponseStatusType.INVALID_PASSWORD.getCode())))
//                .thenReturn(invalidMessage);
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getPassword())).thenReturn(false);
//        when(validator.isValidPassword(userPasswordUpdateRequestDto.getNewPassword())).thenReturn(false);
//        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USER_PASSWORD_URI)
//                .content(userPasswordUpdateRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_PASSWORD.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_PASSWORD.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Start of tests for reset password
//     * Api context : put /api/v1/users/password/reset
//     **/
//
//    @Test
//    void Should_ReturnOk_When_ResetPasswordIsSuccessful() throws Exception {
//        String successMessage = resourceBundleMessageSource.getMessage(SuccessResponseStatusType.RESET_PASSWORD.getCode(),
//                null, locale);
//        when(translator.toLocale(SuccessResponseStatusType.RESET_PASSWORD.getCode()))
//                .thenReturn(successMessage);
//        when(validator.isValidPassword(resetPasswordRequestDto.getPassword())).thenReturn(true);
//        when(userService.resetPassword(any(ResetPasswordRequestDto.class))).thenReturn(user);
//        Collection<OAuth2AccessToken> tokensByUserName = new ArrayList<>();
//        tokensByUserName.add(oAuth2AccessToken);
//
//        when(userService.getTokenByUserId(user.getId())).thenReturn(tokensByUserName);
//        when(defaultTokenServices.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(RESET_USER_PASSWORD_URI)
//                .content(resetPasswordRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(SUCCESS))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.RESET_PASSWORD.getMessage()))
//                .andExpect(jsonPath("$.displayMessage")
//                        .value(successMessage));
//    }
//
//    @Test
//    void Should_ReturnInternalServerError_When_ResetPassword_FailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new AuthServiceException("failed")).when(userService).
//                resetPassword(any(ResetPasswordRequestDto.class));
//        when(validator.isValidPassword(resetPasswordRequestDto.getPassword())).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.put(RESET_USER_PASSWORD_URI)
//                .content(resetPasswordRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_Password_Is_Null_For_ResetPassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        ResetPasswordRequestDto resetPasswordRequestDto = new ResetPasswordRequestDto();
//        mockMvc.perform(MockMvcRequestBuilders.put(RESET_USER_PASSWORD_URI)
//                .content(resetPasswordRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_sWhen_Password_Is_Empty_For_UpdatePassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        ResetPasswordRequestDto resetPasswordRequestDto = new ResetPasswordRequestDto();
//        ReflectionTestUtils.setField(resetPasswordRequestDto, "password", "");
//        mockMvc.perform(MockMvcRequestBuilders.put(RESET_USER_PASSWORD_URI)
//                .content(resetPasswordRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    private Collection<OAuth2AccessToken> oAuth2AccessTokens() {
//        Collection<OAuth2AccessToken> accessTokens = new ArrayList<>();
//        return accessTokens;
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_Input_Password_Is_Invalid_For_ResetPassword() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INVALID_PASSWORD.getCodeString(ErrorResponseStatusType
//                                .INVALID_PASSWORD.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INVALID_PASSWORD
//                .getCodeString(ErrorResponseStatusType.INVALID_PASSWORD.getCode())))
//                .thenReturn(invalidMessage);
//
//        ResetPasswordRequestDto resetPasswordRequestDto = getSampleResetPasswordRequestDto();
//        ReflectionTestUtils.setField(resetPasswordRequestDto, "password", "psw");
//        ReflectionTestUtils.setField(resetPasswordRequestDto, "token", "Tk-1");
//
//        when(validator.isValidPassword(resetPasswordRequestDto.getPassword())).thenReturn(false);
//
//        when(userService.resetPassword(any(ResetPasswordRequestDto.class))).thenReturn(user);
//
//        when(userService.getTokenByUserId(USER_ID)).thenReturn(oAuth2AccessTokens());
//
//        mockMvc.perform(MockMvcRequestBuilders.put(RESET_USER_PASSWORD_URI)
//                .content(resetPasswordRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INVALID_PASSWORD.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INVALID_PASSWORD.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_BadRequest_When_ResetPassword_FailedDueNoUserFound() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .NO_USER.getCodeString(ErrorResponseStatusType
//                                .NO_USER.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.NO_USER
//                .getCodeString(ErrorResponseStatusType.NO_USER.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new InvalidUserException("failed")).when(userService).
//                resetPassword(any(ResetPasswordRequestDto.class));
//        when(validator.isValidPassword(resetPasswordRequestDto.getPassword())).thenReturn(true);
//        mockMvc.perform(MockMvcRequestBuilders.put(RESET_USER_PASSWORD_URI)
//                .content(resetPasswordRequestDto.toJson())
//                .header(X_USER_ID, USER_ID)
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.NO_USER.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.NO_USER.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Get Updated Mobile Numbers
//     */
//
//    @Test
//    void Should_ReturnOk_When_Getting_UpdatedMobileNumbers() throws Exception {
//        String successMessage = "Successfully returned updated mobile numbers.";
//        when(translator.toLocale(SuccessResponseStatusType.GET_USER_UPDATED_MOBILE_NO.getCode()))
//                .thenReturn(successMessage);
//        Pageable pageable2 = PageRequest.of(0, 250, Sort.Direction.ASC, "createdAt");
//        Page<UserMobileNo> page = mock(Page.class);
//        when(page.getTotalPages()).thenReturn(1);
//        when(page.getPageable()).thenReturn(pageable2);
//        when(page.getContent()).thenReturn(Collections.singletonList(userMobileNo));
//        List<UpdatedMobileNoResponseDto> updatedMobileNoList = new ArrayList<>();
//        updatedMobileNoList.add(new UpdatedMobileNoResponseDto(userMobileNo));
//        UpdatedMobileNoListResponseDto updatedMobileNoListResponseDto = new UpdatedMobileNoListResponseDto(updatedMobileNoList, page);
//        updatedMobileNoListResponseDto.setUpdatedMobileNumbers(updatedMobileNoList);
//        when(userService.getUpdateMobileNoList(pageable)).thenReturn(updatedMobileNoListResponseDto);
//        mockMvc.perform(MockMvcRequestBuilders.get(GET_UPDATED_NUMBERS_URI)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .header("app-key", "key"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.GET_USER_UPDATED_MOBILE_NO.getMessage()))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage));
//    }
//
//
//    @Test
//    void Should_ReturnInternalServerError_When_Getting_UpdatedMobileNumbers_FailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new AuthServiceException("failed")).when(userService).
//                getUpdateMobileNoList(any(Pageable.class));
//        mockMvc.perform(MockMvcRequestBuilders.get(GET_UPDATED_NUMBERS_URI)
//                .header("app-key", "key")
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    /**
//     * Get bulk users
//     */
//    @Test
//    void Should_ReturnInternalServerError_When_Getting_BulkUsers_FailedDueToInternalError() throws Exception {
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .INTERNAL_SERVER_ERROR.getCodeString(ErrorResponseStatusType
//                                .INTERNAL_SERVER_ERROR.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
//                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new AuthServiceException("failed")).when(userService).
//                getUserList(userId());
//        mockMvc.perform(MockMvcRequestBuilders.post(GET_BULK_USERS)
//                .content(getBulkUserRequestDto().toLogJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnBadRequest_When_EmptyRequestFor_Getting_BulkUser() throws Exception {
//        BulkUserRequestDto bulkUserRequestDto = getBulkUserRequestDto();
//        bulkUserRequestDto.setUserIds(null);
//        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType
//                        .MISSING_REQUIRED_FIELDS.getCodeString(ErrorResponseStatusType
//                                .MISSING_REQUIRED_FIELDS.getCode()),
//                null, locale);
//        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
//                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
//                .thenReturn(invalidMessage);
//        doThrow(new AuthServiceException("failed")).when(userService).
//                getUserList(userId());
//        mockMvc.perform(MockMvcRequestBuilders.post(GET_BULK_USERS)
//                .content(bulkUserRequestDto.toLogJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
//                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getMessage()))
//                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()))
//                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
//    }
//
//    @Test
//    void Should_ReturnOk_When_Getting_BulkUserSuccess() throws Exception {
//        String successMessage = "Successfully returned user.";
//        when(translator.toLocale(SuccessResponseStatusType.GET_USERS.getCode()))
//                .thenReturn(successMessage);
//        BasicUserResponseDto blogUserResponseDto =
//                new BasicUserResponseDto("uid-1", "user-1", "http://s3-test/fid123");
//        List<BasicUserResponseDto> blogUserResponseDtoList = new ArrayList<>();
//        blogUserResponseDtoList.add(blogUserResponseDto);
//        when(userService.getUserList(userId())).thenReturn(blogUserResponseDtoList);
//        mockMvc.perform(MockMvcRequestBuilders.post(GET_BULK_USERS)
//                .content(getBulkUserRequestDto().toLogJson())
//                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
//                .andExpect(jsonPath("$.status").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.GET_USERS.getMessage()))
//                .andExpect(jsonPath("$.displayMessage").value(successMessage));
//    }
//
//
//    private UserMobileNo getUserMobileNo() {
//        UserMobileUpdateRequestDto userMobileUpdateRequestDto = new UserMobileUpdateRequestDto();
//        userMobileUpdateRequestDto.setMobileNo(getMobileNoRequestDto());
//        return new UserMobileNo(userMobileUpdateRequestDto.getMobileNo().getNo(), USER_ID);
//    }
//
//    private MobileNoRequestDto getMobileNoRequestDto() {
//        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
//        mobileNoRequestDto.setCountryCode(COUNTRY_CODE);
//        mobileNoRequestDto.setLocalNumber(LOCAL_NUMBER);
//        return mobileNoRequestDto;
//    }
//
//    private UserUpdateRequestDto getSampleUserUpdateRequestDto() {
//        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
//        userUpdateRequestDto.setFullName(USER_NAME);
//        return userUpdateRequestDto;
//    }
//
//    private UserPasswordUpdateRequestDto getSamplePasswordUpdateRequestDto() {
//        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = new UserPasswordUpdateRequestDto();
//        userPasswordUpdateRequestDto.setPassword("Tokoin@123");
//        userPasswordUpdateRequestDto.setNewPassword("Tokoin@1234");
//        return userPasswordUpdateRequestDto;
//    }
//
//    private ResetPasswordRequestDto getSampleResetPasswordRequestDto() {
//        ResetPasswordRequestDto resetPasswordRequestDto = new ResetPasswordRequestDto();
//        ReflectionTestUtils.setField(resetPasswordRequestDto, "password", USER_PASS);
//        ReflectionTestUtils.setField(resetPasswordRequestDto, "token", "Tk-1");
//        return resetPasswordRequestDto;
//    }
//
//    private UnregisterUserDto getUnregisterUserDto() {
//        UnregisterUserDto unregisterUserDto = new UnregisterUserDto();
//        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
//        mobileNoRequestDto.setLocalNumber(LOCAL_NUMBER);
//        mobileNoRequestDto.setCountryCode(COUNTRY_CODE);
//        unregisterUserDto.setMobileNo(mobileNoRequestDto);
//        return unregisterUserDto;
//    }
//
//    private UserMobileUpdateRequestDto getSampleMobileUpdateRequestDto() {
//        UserMobileUpdateRequestDto userMobileUpdateRequestDto = new UserMobileUpdateRequestDto();
//        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
//        mobileNoRequestDto.setCountryCode(COUNTRY_CODE);
//        mobileNoRequestDto.setLocalNumber(LOCAL_NUMBER);
//        userMobileUpdateRequestDto.setMobileNo(mobileNoRequestDto);
//        return userMobileUpdateRequestDto;
//    }
//
//    private UserEmailUpdateRequestDto getSampleEmailUpdateRequestDto() {
//        UserEmailUpdateRequestDto userEmailUpdateRequestDto = new UserEmailUpdateRequestDto();
//        userEmailUpdateRequestDto.setEmail(USER_EMAIL);
//        return userEmailUpdateRequestDto;
//    }
//
//    private UserRequestDto getSampleUserRequestDto() {
//        UserRequestDto userRequestDto = new UserRequestDto();
//        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
//        mobileNoRequestDto.setCountryCode(COUNTRY_CODE);
//        mobileNoRequestDto.setLocalNumber(LOCAL_NUMBER);
//        userRequestDto.setFullName(USER_NAME);
//        userRequestDto.setEmail(USER_EMAIL);
//        userRequestDto.setPassword(USER_PASS);
//        userRequestDto.setMobileNo(mobileNoRequestDto);
//        userRequestDto.setLanguage(USER_LANG);
//        return userRequestDto;
//    }
//
//    private User getSampleUser() {
//        User user = new User();
//        user.setId(USER_ID);
//        user.setFullName("user1");
//        user.setEmail(USER_EMAIL);
//        user.setMobileNo(USER_MOBILE_NO);
//        user.setImageUrl("");
//        user.setLanguage(Language.valueOf("ENGLISH"));
//        user.setEnabled(true);
//        user.setRegisteredUser(true);
//        return user;
//    }
//
//    private List<String> userId() {
//        List<String> userIds = new ArrayList<>();
//        userIds.add("uid-1");
//        userIds.add("uid-2");
//        userIds.add("uid-3");
//        return userIds;
//    }
//
//    private BulkUserRequestDto getBulkUserRequestDto() {
//        BulkUserRequestDto bulkUserRequestDto = new BulkUserRequestDto();
//        bulkUserRequestDto.setUserIds(userId());
//        return bulkUserRequestDto;
//    }
//
//}
