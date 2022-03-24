package com.swivel.cc.auth.util.validator;

import com.swivel.cc.auth.util.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the Validator class.
 */
class ValidatorTest {

    private static final String EMAIL = "rengy@tokoin.io";
    private static final String MOBILE_NO = "+94-7712345678";
    private static final String IMAGE_URL = "https://tokobook.s3.ap-southeast-1.amazonaws.com/test";

    private final Validator validator = new Validator();

    /**
     * Email
     */

    @Test
    void Should_ReturnTrue_When_EmailIsValid() {
        assertTrue(validator.isValidEmail(EMAIL));
    }

    @Test
    void Should_ReturnFalse_When_EmailIsWithoutDot() {
        assertFalse(validator.isValidEmail("rengy@tokoinio"));
    }

    @Test
    void Should_ReturnFalse_When_EmailIsWithoutAt() {
        assertFalse(validator.isValidEmail("rengytokoin.io"));
    }

    /**
     * Mobile No with country code
     */

    @Test
    void Should_ReturnTrue_When_MobileNoIsValid() {
        assertTrue(validator.isValidMobileNoWithCountryCode(MOBILE_NO));
    }

    @Test
    void Should_ReturnFalse_When_MobileNoWithoutCountryCode() {
        assertFalse(validator.isValidMobileNoWithCountryCode("081123123"));
    }

    @Test
    void Should_ReturnFalse_When_MobileNoWithoutAllDigits() {
        assertFalse(validator.isValidMobileNoWithCountryCode("+6281"));
    }

    @Test
    void Should_ReturnFalse_When_MobileNoHasSpaceInTheMiddle() {
        assertFalse(validator.isValidMobileNoWithCountryCode("+947233 45345"));
    }

    @Test
    void Should_ReturnFalse_For_NumberWithMoreThanFiveDigitCountryCode() {
        assertFalse(validator.isValidMobileNoWithCountryCode("+623452-808733998"));
    }

    /**
     * Password
     */

    @Test
    void Should_ReturnTrue_When_PasswordIsValid() {
        assertTrue(validator.isValidPassword("Toko12"));
    }

    @Test
    void Should_ReturnFalse_When_PasswordWithNoUpperCase() {
        assertFalse(validator.isValidPassword("toko@1"));
    }

    @Test
    void Should_ReturnFalse_When_PasswordWithNoLowerCase() {
        assertFalse(validator.isValidPassword("TOKO@1"));
    }

    @Test
    void Should_ReturnFalse_When_PasswordWith5Characters() {
        assertFalse(validator.isValidPassword("Tok@1"));
    }

    @Test
    void Should_ReturnFalse_When_PasswordWith6Numbers() {
        assertFalse(validator.isValidPassword("123456"));
    }

    @Test
    void Should_ReturnTrue_When_RandomValidPassword() {
        assertTrue(validator.isValidPassword("Ra@1aedae1223"));
    }

    /**
     * username
     */
    @Test
    void Should_ReturnTrue_When_ValidMobileNoIsGivenAsUsername() {
        assertTrue(validator.isValidUsername(MOBILE_NO));
    }

    @Test
    void Should_ReturnTrue_When_ValidEmailIsGivenAsUsername() {
        assertTrue(validator.isValidUsername(EMAIL));
    }

    @Test
    void Should_ReturnFalse_When_InValidUsernameIsGiven() {
        assertFalse(validator.isValidUsername("test"));
    }

    /**
     * Image Url
     */
    @Test
    void Should_ReturnTrue_When_ValidatedImageUrlIsGiven() {
        assertTrue(validator.isValidUrl(IMAGE_URL));
    }

    @Test
    void Should_ReturnFalse_When_InValidImageUrlIsGiven() {
        assertFalse(validator.isValidUrl("test"));
    }
}