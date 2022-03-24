package com.swivel.cc.auth.domain.request;

import com.swivel.cc.auth.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests the {@link BusinessRequestDto } class.
 */
@Slf4j
class BusinessRequestDtoTest {

    private static final String MERCHANT_ID = "uid-bd0c8894-8ddb-4b1d-8383-de44bfaa297f";
    private static final String BUSINESS_NAME = "Singer";
    private static final String OWNER_NAME = "Owner";
    private static final String EMAIL = "singer@gmail.com";
    private static final String WEB_SITE = "https://www.singersl.com/";
    private static final String IMAGE_URL = "https://www.singersl.com/image.jpg";
    private static final MobileNoRequestDto TELEPHONE =
            new MobileNoRequestDto("+94", "71332191");
    private static final MobileNoRequestDto INVALID_TELEPHONE =
            new MobileNoRequestDto("+94", "07133219e1");

    @Mock
    private Validator validator;

    @BeforeEach
    void setUp() {
        initMocks(this);
        when(validator.isValidMobileNoWithCountryCode("+94-71332191")).thenReturn(true);
        when(validator.isValidEmail(EMAIL)).thenReturn(true);
        when(validator.isValidUrl(WEB_SITE)).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_ReturnTrue_When_RequiredFieldsAreAvailable() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        assertTrue(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_RequiredFieldsAreNotAvailable() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setMerchantId("");
        businessRequestDto.setBusinessName("");
        businessRequestDto.setOwnerName("");
        businessRequestDto.setTelephone(new MobileNoRequestDto("+94", ""));
        assertFalse(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_RequiredFieldsAreNull() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setMerchantId(null);
        businessRequestDto.setBusinessName(null);
        businessRequestDto.setOwnerName(null);
        businessRequestDto.setTelephone(null);
        assertFalse(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_MerchantIdIsNotAvailable() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setMerchantId("");
        assertFalse(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_MerchantIdIsNull() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setMerchantId(null);
        assertFalse(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_BusinessNameIsNotAvailable() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setBusinessName("");
        assertFalse(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_BusinessNameIsNull() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setBusinessName(null);
        assertFalse(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_OwnerNameIsNotAvailable() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setOwnerName("");
        assertFalse(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnFalse_When_OwnerNameIsNull() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setOwnerName(null);
        assertFalse(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnTrue_When_TelephoneIsNull() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setTelephone(null);
        assertTrue(businessRequestDto.isRequiredAvailable());
    }

    @Test
    void Should_ReturnTrue_When_TelephoneNoIsValid() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        assertTrue(businessRequestDto.validateTelephoneNumberIfPresent(validator));
    }

    @Test
    void Should_ReturnFalse_When_TelephoneNoIsNotValid() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setTelephone(INVALID_TELEPHONE);
        assertFalse(businessRequestDto.validateTelephoneNumberIfPresent(validator));
    }

    @Test
    void Should_ReturnTrue_When_EmailIsValid() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        assertTrue(businessRequestDto.isContainingValidEmail(validator));
    }

    @Test
    void Should_ReturnFalse_When_EmailIsNotValid() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setEmail("singer#gmail.com");
        assertFalse(businessRequestDto.isContainingValidEmail(validator));
    }

    @Test
    void Should_ReturnTrue_When_EmailIsEmpty() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setEmail("");
        assertTrue(businessRequestDto.isContainingValidEmail(validator));
    }

    @Test
    void Should_ReturnTrue_When_EmailIsNull() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setEmail(null);
        assertTrue(businessRequestDto.isContainingValidEmail(validator));
    }

    @Test
    void Should_ReturnTrue_When_UrlIsValid() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        assertTrue(businessRequestDto.isContainingValidUrls(validator));
    }

    @Test
    void Should_ReturnFalse_When_UrlIsNotValid() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setWebSite("htt://www.singersl.com/");
        assertFalse(businessRequestDto.isContainingValidUrls(validator));
    }

    @Test
    void Should_ReturnTrue_When_UrlIsEmpty() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setWebSite("");
        assertTrue(businessRequestDto.isContainingValidUrls(validator));
    }

    @Test
    void Should_ReturnTrue_When_UrlIsNull() {
        BusinessRequestDto businessRequestDto = getBusinessRequestDto();
        businessRequestDto.setWebSite(null);
        assertTrue(businessRequestDto.isContainingValidUrls(validator));
    }

    private BusinessRequestDto getBusinessRequestDto() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();
        businessRequestDto.setMerchantId(MERCHANT_ID);
        businessRequestDto.setBusinessName(BUSINESS_NAME);
        businessRequestDto.setOwnerName(OWNER_NAME);
        businessRequestDto.setTelephone(TELEPHONE);
        businessRequestDto.setImageUrl(IMAGE_URL);
        businessRequestDto.setEmail(EMAIL);
        businessRequestDto.setWebSite(WEB_SITE);
        return businessRequestDto;
    }

}