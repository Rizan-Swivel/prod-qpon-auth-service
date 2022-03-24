package com.swivel.cc.auth.controller;

import com.swivel.cc.auth.configuration.ResourceBundleMessageSourceBean;
import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.domain.entity.Role;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.domain.request.BusinessRequestDto;
import com.swivel.cc.auth.domain.request.MobileNoRequestDto;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.enums.RoleType;
import com.swivel.cc.auth.enums.SuccessResponseStatusType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.service.MerchantService;
import com.swivel.cc.auth.service.UserService;
import com.swivel.cc.auth.util.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class tests the {@link MerchantController } class.
 */
class MerchantControllerTest {

    private static final String MERCHANT_ID = "uid-bd0c8894-8ddb-4b1d-8383-de44bfaa297f";
    private static final String BUSINESS_ID = "bisid-123";
    private static final String BUSINESS_NAME = "Singer";
    private static final String OWNER_NAME = "Owner";
    private static final String EMAIL = "singer@gmail.com";
    private static final String WEB_SITE = "https://www.singersl.com/";
    private static final String IMAGE_URL = "https://www.singersl.com/image.jpg";
    private static final String APPLICATION_JSON_UTF_8 = "application/json;charset=UTF-8";
    private static final String TIME_ZONE = "Asia/Colombo";
    private static final String CREATE_OR_UPDATE_BUSINESS_URI = "/api/v1/users/MERCHANT/business";
    private final MobileNoRequestDto mobileNo = new MobileNoRequestDto("+94", "713321911");
    private final MobileNoRequestDto telephone = new MobileNoRequestDto("+94", "1123564889");
    private final Business business = getSampleBusiness();
    private final Locale locale = LocaleContextHolder.getLocale();
    private final BusinessRequestDto businessRequestDto = getSampleBusinessRequestDto();
    private final ResourceBundleMessageSourceBean resourceBundleMessageSourceBean = new ResourceBundleMessageSourceBean();
    private MockMvc mockMvc;
    private ResourceBundleMessageSource resourceBundleMessageSource;

    @Mock
    private MerchantService merchantService;
    @Mock
    private Validator validator;
    @Mock
    private UserService userService;
    @Mock
    private Translator translator;


    @BeforeEach
    void setUp() {
        initMocks(this);
        MerchantController merchantController = new MerchantController(translator, merchantService, validator, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(merchantController).build();
        this.resourceBundleMessageSource = resourceBundleMessageSourceBean.messageSource();

        when(validator.isValidMobileNoWithCountryCode("+94-1123564889")).thenReturn(true);
        when(validator.isValidMobileNoWithCountryCode("+94-713321911")).thenReturn(true);
        when(validator.isValidEmail(EMAIL)).thenReturn(true);
        when(validator.isValidUrl(WEB_SITE)).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_ReturnOk_When_CreatingOrUpdatingBusinessInfo() throws Exception {
        String successMessage = resourceBundleMessageSource
                .getMessage(SuccessResponseStatusType.CREATED_BUSINESS_PROFILE.getCode(), null, locale);
        when(translator.toLocale(SuccessResponseStatusType.CREATED_BUSINESS_PROFILE.getCode()))
                .thenReturn(successMessage);
        when(merchantService.createOrUpdateBusiness(any(BusinessRequestDto.class), eq(RoleType.MERCHANT)))
                .thenReturn(business);
        mockMvc.perform(MockMvcRequestBuilders.put(CREATE_OR_UPDATE_BUSINESS_URI)
                        .header("Time-Zone", TIME_ZONE)
                        .content(businessRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.message", is("Successfully created business profile")))
                .andExpect(jsonPath("$.data.businessId", startsWith("bisid-")))
                .andExpect(jsonPath("$.displayMessage").value(successMessage));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingOrUpdatingBusinessInfoWithoutRequiredFields() throws Exception {
        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode()), null, locale);
        when(translator.toLocale(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
                .getCodeString(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS.getCode())))
                .thenReturn(invalidMessage);
        businessRequestDto.setMerchantId(null);
        mockMvc.perform(MockMvcRequestBuilders.put(CREATE_OR_UPDATE_BUSINESS_URI)
                        .header("Time-Zone", TIME_ZONE)
                        .content(businessRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Required fields are missing")))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
                        .MISSING_REQUIRED_FIELDS.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingOrUpdatingBusinessInfoWithInvalidTelephoneNo() throws Exception {
        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType.INVALID_TELEPHONE_NUMBER
                .getCodeString(ErrorResponseStatusType.INVALID_TELEPHONE_NUMBER.getCode()), null, locale);
        when(translator.toLocale(ErrorResponseStatusType.INVALID_TELEPHONE_NUMBER
                .getCodeString(ErrorResponseStatusType.INVALID_TELEPHONE_NUMBER.getCode())))
                .thenReturn(invalidMessage);
        businessRequestDto.setTelephone(new MobileNoRequestDto("+94", "11235648e9"));
        mockMvc.perform(MockMvcRequestBuilders.put(CREATE_OR_UPDATE_BUSINESS_URI)
                        .header("Time-Zone", TIME_ZONE)
                        .content(businessRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Invalid telephone number.")))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
                        .INVALID_TELEPHONE_NUMBER.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingOrUpdatingBusinessInfoWithInvalidEmailAddress() throws Exception {
        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType.INVALID_EMAIL
                .getCodeString(ErrorResponseStatusType.INVALID_EMAIL.getCode()), null, locale);
        when(translator.toLocale(ErrorResponseStatusType.INVALID_EMAIL
                .getCodeString(ErrorResponseStatusType.INVALID_EMAIL.getCode())))
                .thenReturn(invalidMessage);
        businessRequestDto.setEmail("singer#gmail.com");
        mockMvc.perform(MockMvcRequestBuilders.put(CREATE_OR_UPDATE_BUSINESS_URI)
                        .header("Time-Zone", TIME_ZONE)
                        .content(businessRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Invalid email address")))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
                        .INVALID_EMAIL.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingOrUpdatingBusinessInfoWithInvalidUrls() throws Exception {
        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType.INVALID_URLS
                .getCodeString(ErrorResponseStatusType.INVALID_URLS.getCode()), null, locale);
        when(translator.toLocale(ErrorResponseStatusType.INVALID_URLS
                .getCodeString(ErrorResponseStatusType.INVALID_URLS.getCode())))
                .thenReturn(invalidMessage);
        businessRequestDto.setWebSite("htt://www.singersl.com/");
        mockMvc.perform(MockMvcRequestBuilders.put(CREATE_OR_UPDATE_BUSINESS_URI)
                        .header("Time-Zone", TIME_ZONE)
                        .content(businessRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Invalid URL for website or facebook or instagram.")))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
                        .INVALID_URLS.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingOrUpdatingBusinessInfoWithInvalidTimezone() throws Exception {
        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType.INVALID_TIMEZONE
                .getCodeString(ErrorResponseStatusType.INVALID_TIMEZONE.getCode()), null, locale);
        when(translator.toLocale(ErrorResponseStatusType.INVALID_TIMEZONE
                .getCodeString(ErrorResponseStatusType.INVALID_TIMEZONE.getCode())))
                .thenReturn(invalidMessage);
        mockMvc.perform(MockMvcRequestBuilders.put(CREATE_OR_UPDATE_BUSINESS_URI)
                        .header("Time-Zone", "AsiaColombo")
                        .content(businessRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Invalid time zone.")))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
                        .INVALID_TIMEZONE.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingOrUpdatingBusinessInfoForInvalidMerchantId() throws Exception {
        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType.INVALID_MERCHANT_ID
                .getCodeString(ErrorResponseStatusType.INVALID_MERCHANT_ID.getCode()), null, locale);
        when(translator.toLocale(ErrorResponseStatusType.INVALID_MERCHANT_ID
                .getCodeString(ErrorResponseStatusType.INVALID_MERCHANT_ID.getCode())))
                .thenReturn(invalidMessage);
        when(merchantService.createOrUpdateBusiness(any(BusinessRequestDto.class), eq(RoleType.MERCHANT)))
                .thenThrow(new InvalidUserException("Failed") {
                });
        mockMvc.perform(MockMvcRequestBuilders.put(CREATE_OR_UPDATE_BUSINESS_URI)
                        .header("Time-Zone", TIME_ZONE)
                        .content(businessRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Invalid merchant id")))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
                        .INVALID_MERCHANT_ID.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
    }

    @Test
    void Should_ReturnInternalServerError_When_CreatingOrUpdatingBusinessInfo() throws Exception {
        String invalidMessage = resourceBundleMessageSource.getMessage(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode()), null, locale);
        when(translator.toLocale(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                .getCodeString(ErrorResponseStatusType.INTERNAL_SERVER_ERROR.getCode())))
                .thenReturn(invalidMessage);
        when(merchantService.createOrUpdateBusiness(any(BusinessRequestDto.class), eq(RoleType.MERCHANT)))
                .thenThrow(new AuthServiceException("Failed") {
                });
        mockMvc.perform(MockMvcRequestBuilders.put(CREATE_OR_UPDATE_BUSINESS_URI)
                        .header("Time-Zone", TIME_ZONE)
                        .content(businessRequestDto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(APPLICATION_JSON_UTF_8))
                .andExpect(jsonPath("$.status", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Failed due to an internal server error")))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
                        .INTERNAL_SERVER_ERROR.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(invalidMessage));
    }

    private BusinessRequestDto getSampleBusinessRequestDto() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();
        businessRequestDto.setMerchantId(MERCHANT_ID);
        businessRequestDto.setBusinessName(BUSINESS_NAME);
        businessRequestDto.setOwnerName(OWNER_NAME);
        businessRequestDto.setMobileNo(mobileNo);
        businessRequestDto.setTelephone(telephone);
        businessRequestDto.setImageUrl(IMAGE_URL);
        businessRequestDto.setEmail(EMAIL);
        businessRequestDto.setWebSite(WEB_SITE);
        return businessRequestDto;
    }

    private Business getSampleBusiness() {
        User merchant = new User();
        merchant.setId(MERCHANT_ID);
        merchant.setRole(new Role(RoleType.MERCHANT));

        Business business = new Business();
        business.setId(BUSINESS_ID);
        business.setMerchant(merchant);
        business.setBusinessName(BUSINESS_NAME);
        business.setOwnerName(OWNER_NAME);
        business.setMobileNo(mobileNo.getNo());
        business.setTelephone(telephone.getNo());
        business.setImageUrl(IMAGE_URL);
        business.setEmail(EMAIL);
        business.setWebSite(WEB_SITE);
        business.setCreatedAt(new Date());
        business.setUpdatedAt(new Date());
        return business;
    }
}