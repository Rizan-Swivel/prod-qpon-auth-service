package com.swivel.cc.auth.enums;

import com.swivel.cc.auth.exception.AuthServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link Language} class.
 */
class LanguageTest {

    private static final String INVALID_LANGUAGE = "Invalid language";

    @Test
    void Should_ReturnTrue_When_LanguageIsBahasa() {
        assertTrue(Language.isValidLanguage("Bahasa "));
    }

    @Test
    void Should_ReturnTrue_When_LanguageIsEnglish() {
        assertTrue(Language.isValidLanguage("english"));
    }

    @Test
    void Should_ReturnFalse_When_LanguageIsUnsupported() {
        assertFalse(Language.isValidLanguage("Something else"));
    }

    @Test
    void Should_ReturnFalse_When_LanguageIsNull() {
        assertFalse(Language.isValidLanguage(null));
    }

    @Test
    void Should_ReturnFalse_When_LanguageIsEmpty() {
        assertFalse(Language.isValidLanguage(" "));
    }

    @Test
    void Should_ReturnBahasa_When_LanguageIsBahasa() {
        assertEquals(Language.BAHASA, Language.getLanguage("Bahasa "));
    }

    @Test
    void Should_ReturnEnglish_When_LanguageIsEnglish() {
        assertEquals(Language.ENGLISH, Language.getLanguage("english"));
    }

    @Test
    void Should_ThrowException_When_LanguageIsUnsupported() {
        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> Language.getLanguage("test"));
        assertEquals(INVALID_LANGUAGE, exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_LanguageIsNull() {
        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> Language.getLanguage(null));
        assertEquals(INVALID_LANGUAGE, exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_LanguageIsEmpty() {
        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> Language.getLanguage(" "));
        assertEquals(INVALID_LANGUAGE, exception.getMessage());
    }
}