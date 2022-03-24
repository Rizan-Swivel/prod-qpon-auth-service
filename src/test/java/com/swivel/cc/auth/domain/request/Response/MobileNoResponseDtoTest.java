package com.swivel.cc.auth.domain.request.Response;

import com.swivel.cc.auth.domain.response.MobileNoResponseDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the {@link MobileNoResponseDto} class
 */
class MobileNoResponseDtoTest {

    @Test
    void Should_ReturnMobileNo_When_ValidMobileNoIsGiven() {
        MobileNoResponseDto mobileNoResponseDto = new MobileNoResponseDto("+94-713321911");
        assertEquals("+94", mobileNoResponseDto.getCountryCode());
        assertEquals("713321911", mobileNoResponseDto.getLocalNumber());
    }

    @Test
    void Should_ReturnNull_When_NullMobileNoIsGiven() {
        MobileNoResponseDto mobileNoResponseDto = new MobileNoResponseDto(null);
        assertNull(mobileNoResponseDto.getCountryCode());
        assertNull(mobileNoResponseDto.getLocalNumber());
    }

    @Test
    void Should_ReturnNull_When_EmptyMobileNoIsGiven() {
        MobileNoResponseDto mobileNoResponseDto = new MobileNoResponseDto(" ");
        assertNull(mobileNoResponseDto.getCountryCode());
        assertNull(mobileNoResponseDto.getLocalNumber());
    }

    @Test
    void Should_ReturnNull_When_InvalidMobileNoIsGiven() {
        MobileNoResponseDto mobileNoResponseDto = new MobileNoResponseDto("0713321911");
        assertNull(mobileNoResponseDto.getCountryCode());
        assertNull(mobileNoResponseDto.getLocalNumber());
    }
}