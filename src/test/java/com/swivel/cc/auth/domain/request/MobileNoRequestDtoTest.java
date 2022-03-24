package com.swivel.cc.auth.domain.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the {@link MobileNoRequestDto} class
 */
class MobileNoRequestDtoTest {

    private static final String LOCAL_MOBILE_NO = "0713321911";
    private static final String MOBILE_NO_WITH_COUNTRY_CODE = "+94-0713321911";

    @Test
    void Should_ReturnMobileNo_When_MobileNoIsProvidedWithCountryCode() {
        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto("+94", LOCAL_MOBILE_NO);
        assertEquals(MOBILE_NO_WITH_COUNTRY_CODE, mobileNoRequestDto.getNo());
    }


}