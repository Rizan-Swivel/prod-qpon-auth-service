package com.swivel.cc.auth.service;

import com.swivel.cc.auth.domain.entity.PasswordResetToken;
import com.swivel.cc.auth.domain.entity.Role;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.domain.entity.UserMobileNo;
import com.swivel.cc.auth.domain.request.*;
import com.swivel.cc.auth.enums.Language;
import com.swivel.cc.auth.enums.RoleType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidPasswordException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests the {@link UserService } class.
 */
class UserServiceTest {

    private static final String USER_EMAIL = "rengy@tokoin.io";
    private static final String USER_PASS = "Tokoin@123";
    private static final String NEW_USER_PASS = "Tokoin@1234";
    private static final String USER_MOBILE_NO = "+62-81123123";
    private static final String USER_ID = "uid-1234567890";
    private static final String IMAGE_URL = "http://localhost:8080/image.png";
    private static final String COUNTRY_CODE = "+62";
    private static final String USER_NAME = "Rendy Deraman";
    private static final String LOCAL_NUMBER = "81123123";
    private static final String PASSWORD_RESET_TOKEN = "psw_token";
    private final UserUpdateRequestDto userUpdateRequestDto = getSampleUserUpdateRequestDto();
    private final UserMobileNo userMobileNo = getUserMobileNo();
    private final User user = getSampleUser();
    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JdbcTokenStore jdbcTokenStore;
    @Mock
    private OAuth2AccessToken oAuth2AccessToken;
    @Mock
    private UserMobileRepository userMobileRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private MerchantService merchantService;
    @Mock
    private MerchantBankSearchIndexRepository merchantBankSearchIndexRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ApprovedBankBusinessRepository approvedBankBusinessRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userService = new UserService(userRepository, passwordEncoder, jdbcTokenStore,
                userMobileRepository, passwordResetTokenRepository, roleRepository, notificationService,
                merchantService, merchantBankSearchIndexRepository, approvedBankBusinessRepository);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Mobile no existence
     */

    @Test
    void Should_ReturnTrue_When_MobileNoIsExisted() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(new User());
        assertTrue(userService.isMobileNoExist(USER_MOBILE_NO));
    }

    @Test
    void Should_ReturnFalse_When_MobileNoIsNotExisted() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(null);
        assertFalse(userService.isMobileNoExist(USER_MOBILE_NO));
    }

    @Test
    void Should_ThrowException_When_GettingMobileNoIsFailed() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenThrow(new DataAccessException("failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.isMobileNoExist(USER_MOBILE_NO));
        assertEquals("Checking mobile no existence was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowException_For_NonExisting_Mobile_No() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(null);
        InvalidUserException exception = assertThrows(InvalidUserException.class, () ->
                userService.getUserByUserId(USER_MOBILE_NO));
        assertEquals("Invalid user id", exception.getMessage());
    }

    /**
     * Email existence
     */

    @Test
    void Should_ReturnTrue_When_EmailIsExisted() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(new User());
        assertTrue(userService.isEmailExist(USER_EMAIL));
    }

    @Test
    void Should_ReturnFalse_When_EmailIsNotExisted() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(null);
        assertFalse(userService.isEmailExist(USER_EMAIL));
    }

    @Test
    void Should_ThrowException_When_GettingEmailIsFailed() {
        when(userRepository.findByEmail(USER_EMAIL)).thenThrow(new DataAccessException("failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.isEmailExist(USER_EMAIL));
        assertEquals("Checking email existence was failed", exception.getMessage());
    }

    /**
     * Create user
     */

    @Test
    void Should_SaveUser() {
        when(userRepository.save(user)).thenReturn(user);
        userService.createUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void Should_ThrowException_When_SavingUserIsFailed() {
        when(userRepository.save(user)).thenThrow(new DataAccessException("failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.createUser(user));
        assertEquals("Saving user to database was failed", exception.getMessage());
    }

    /**
     * Create Unregister User
     */
    @Test
    void Should_SaveUnRegUser() {
        when(userRepository.save(user)).thenReturn(user);
        userService.createUnregisteredUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void ShouldReturnFalse_When_AlreadyExistingMobileNoFor_SaveUnRegUser() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(new User());
        assertTrue(userService.isMobileNoExist(USER_MOBILE_NO));
    }

    @Test
    void ShouldThrowException_When_AlreadyExistingMobileNoFor_SaveUnRegUser() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(user);

        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.createUnregisteredUser(user));
        assertEquals("Already existing mobile number", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_SavingUnRegUserIsFailed() {
        when(userRepository.save(user)).thenThrow(new DataAccessException("failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.createUnregisteredUser(user));
        assertEquals("Saving user to database was failed", exception.getMessage());
    }

    /**
     * Getting user
     */
    @Test
    void Should_ReturnUser_When_MobileNoIsGivenAsUsername() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(user);
        assertEquals(user, userService.getUserByMobileNo(USER_MOBILE_NO));
    }

    @Test
    void Should_ReturnUser_When_UserId_IsGiven() {
        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.ofNullable(user));
        assertEquals(user, userService.getUserByUserId(USER_ID));
    }

    @Test
    void ShouldThrowException_When_InvalidMobileNo() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(null);
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.getUserByMobileNo(USER_MOBILE_NO));
        assertEquals("Invalid mobile number", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_GetUserMobileForInvalidUserId() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO))
                .thenReturn(user);
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.getUserByUserId(USER_MOBILE_NO);
        });
        assertEquals("Invalid user id", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_GettingUserByIdWasFailed() {
        when(userRepository.findById(USER_ID)).thenThrow(new DataAccessException("Failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.getUserByUserId(USER_ID));
        assertEquals("Getting user from database was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_GettingUserByInvalidUserId() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.getUserByUserId(USER_ID));
        assertEquals("Invalid user id", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_GettingUserWasFailed() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenThrow(new DataAccessException("Failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> {
            userService.getUserByMobileNo(USER_MOBILE_NO);
        });
        assertEquals("Getting user from database was failed", exception.getMessage());
    }

    /**
     * Getting user by mobile no
     */
    @Test
    void Should_ReturnUserByMobileNo() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(user);
        User user = userService.getUserByMobileNo(USER_MOBILE_NO);
        assertEquals(USER_MOBILE_NO, user.getMobileNo());
    }

    /**
     * Already existing user
     */

    @Test
    void Should_ReturnTrue_When_UserIsAlreadyRegistered() {
        user.setRegisteredUser(true);
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(user);
        assertTrue(userService.isAlreadyRegisteredUser(USER_MOBILE_NO));
    }

    @Test
    void Should_ReturnFalse_When_UserIsNotRegistered() {
        user.setRegisteredUser(false);
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(user);
        assertFalse(userService.isAlreadyRegisteredUser(USER_MOBILE_NO));
    }

    @Test
    void Should_ReturnFalse_When_UserIsNotRegisteredAndNoMobileNo() {
        when(userRepository.findByMobileNo(USER_MOBILE_NO)).thenReturn(null);
        assertFalse(userService.isAlreadyRegisteredUser(USER_MOBILE_NO));
    }

    /**
     * Update user
     */

    @Test
    void Should_Update_User_When_UserIsValidWithAllFields() {

        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setUserId(USER_ID);
        userUpdateRequestDto.setFullName("fullName");
        userUpdateRequestDto.setImageUrl(IMAGE_URL);

        User user = new User();
        user.setId("uid-1");
        user.setFullName("user1");
        user.setEmail(USER_EMAIL);
        user.setMobileNo(USER_MOBILE_NO);
        user.setImageUrl(IMAGE_URL);
        user.setLanguage(Language.valueOf("ENGLISH"));
        user.setEnabled(true);
        user.setRegisteredUser(true);
        user.setRole(new Role(RoleType.USER));

        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        User result = userService.updateUser(userUpdateRequestDto);

        assertEquals("fullName", result.getFullName());
        assertEquals("uid-1", result.getId());
        assertEquals(USER_MOBILE_NO, result.getMobileNo());
        assertEquals(USER_EMAIL, result.getEmail());
        assertEquals(IMAGE_URL, result.getImageUrl());

    }

    @Test
    void Should_Update_User_When_LanguageIsProvided() {

        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setUserId(USER_ID);
        userUpdateRequestDto.setFullName("fullName");
        userUpdateRequestDto.setImageUrl(IMAGE_URL);

        User user = new User();
        user.setId("uid-1");
        user.setFullName("user1");
        user.setEmail(USER_EMAIL);
        user.setMobileNo(USER_MOBILE_NO);
        user.setImageUrl(IMAGE_URL);
        user.setLanguage(Language.valueOf("ENGLISH"));
        user.setEnabled(true);
        user.setRegisteredUser(true);
        user.setRole(new Role(RoleType.USER));

        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        User result = userService.updateUser(userUpdateRequestDto);

        assertEquals("fullName", result.getFullName());
        assertEquals("uid-1", result.getId());
        assertEquals(USER_MOBILE_NO, result.getMobileNo());
        assertEquals(USER_EMAIL, result.getEmail());
        assertEquals(IMAGE_URL, result.getImageUrl());
        assertEquals(Language.valueOf("ENGLISH"), result.getLanguage());

    }


    @Test
    void Should_Update_User_When_UserIsValidWithoutOptionalFields() {

        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setUserId(USER_ID);
        userUpdateRequestDto.setFullName("fullName");

        User user = new User();
        user.setId("uid-1");
        user.setFullName("user1");
        user.setEmail(USER_EMAIL);
        user.setMobileNo(USER_MOBILE_NO);
        user.setImageUrl(IMAGE_URL);
        user.setLanguage(Language.valueOf("ENGLISH"));
        user.setEnabled(true);
        user.setRegisteredUser(true);
        user.setRole(new Role(RoleType.USER));

        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        User result = userService.updateUser(userUpdateRequestDto);
        assertEquals("fullName", result.getFullName());
        assertEquals("uid-1", result.getId());
        assertEquals(USER_MOBILE_NO, result.getMobileNo());
    }

    @Test
    void Should_ThrowException_When_UpdatingUserForInvalidUserId() {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();

        when(userRepository.findById(userUpdateRequestDto.getUserId()))
                .thenReturn(Optional.empty());
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.updateUser(userUpdateRequestDto);
        });
        assertEquals("Invalid user id", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_UpdatingUserFailed() {

        when(userRepository.findById(userUpdateRequestDto.getUserId())).thenReturn(Optional.ofNullable(user));

        when(userRepository.save(user)).thenThrow(new DataAccessException("failed") {
        });

        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.updateUser(userUpdateRequestDto));

        assertEquals("Updating user to database was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_UpdaterUserWasFailedForInvalidUSer() {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();

        when(userRepository.findById(userUpdateRequestDto.getUserId())).thenThrow(new DataAccessException("Failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> {
            userService.updateUser(userUpdateRequestDto);
        });
        assertEquals("Getting user from database was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_UpdaterUserWasFailed() {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setUserId(USER_ID);
        userUpdateRequestDto.setFullName("user_name");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));

        when(userService.updateUser(userUpdateRequestDto)).thenThrow(new AuthServiceException("Updating user to database was failed"));
        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> {
            userService.updateUser(userUpdateRequestDto);
        });
        assertEquals("Updating user to database was failed", exception.getMessage());
    }

    /**
     * Update email
     */

    @Test
    void Should_Update_Email_When_User_Is_Valid() {

        UserEmailUpdateRequestDto userEmailUpdateRequestDto = new UserEmailUpdateRequestDto();
        userEmailUpdateRequestDto.setUserId(USER_ID);
        userEmailUpdateRequestDto.setEmail(USER_EMAIL);

        User user = new User();
        user.setId("uid-1");
        user.setFullName("user1");
        user.setEmail("user@gmail.com");
        user.setMobileNo("+661323324");
        user.setImageUrl("");
        user.setLanguage(Language.valueOf("ENGLISH"));
        user.setEnabled(true);
        user.setRegisteredUser(true);

        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        User result = userService.updateEmail(userEmailUpdateRequestDto);

        assertEquals("user1", result.getFullName());
        assertEquals("uid-1", result.getId());
        assertEquals("+661323324", result.getMobileNo());

    }


    @Test
    void Should_ThrowException_When_UpdatingEmailWasFailed() {
        UserEmailUpdateRequestDto userEmailUpdateRequestDto = new UserEmailUpdateRequestDto();
        userEmailUpdateRequestDto.setUserId(USER_ID);
        userEmailUpdateRequestDto.setEmail(USER_EMAIL);

        when(userRepository.findById(USER_ID)).thenThrow(new
                AuthServiceException("Updating users to database was failed"));

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> {
            userService.updateEmail(userEmailUpdateRequestDto);
        });
        assertEquals("Updating users to database was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_UpdatingEmailForInvalidUserId() {
        UserEmailUpdateRequestDto userEmailUpdateRequestDto = new UserEmailUpdateRequestDto();
        userEmailUpdateRequestDto.setUserId(USER_ID);
        userEmailUpdateRequestDto.setEmail(USER_EMAIL);

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.empty());
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.updateEmail(userEmailUpdateRequestDto);
        });
        assertEquals("Invalid user id", exception.getMessage());
    }

    /**
     * Update mobile No
     */
    @Test
    void Should_Update_Mobile_No_When_User_Is_Valid() {

        UserMobileUpdateRequestDto userMobileUpdateRequestDto = new UserMobileUpdateRequestDto();
        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
        mobileNoRequestDto.setCountryCode(COUNTRY_CODE);
        mobileNoRequestDto.setLocalNumber(LOCAL_NUMBER);
        userMobileUpdateRequestDto.setUserId(USER_ID);
        userMobileUpdateRequestDto.setMobileNo(mobileNoRequestDto);

        User user = new User();
        user.setId("uid-1");
        user.setFullName("user1");
        user.setEmail("user@gmail.com");
        user.setMobileNo(USER_MOBILE_NO);
        user.setImageUrl("");
        user.setLanguage(Language.valueOf("ENGLISH"));
        user.setEnabled(true);
        user.setRegisteredUser(true);

        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        User result = userService.updateMobileNo(userMobileUpdateRequestDto);

        assertEquals("user1", result.getFullName());
        assertEquals("uid-1", result.getId());
        assertEquals(USER_MOBILE_NO, result.getMobileNo());

    }

    @Test
    void Should_ThrowException_When_UpdatingMobileNoWasFailed() {
        UserMobileUpdateRequestDto userMobileUpdateRequestDto = new UserMobileUpdateRequestDto();
        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
        mobileNoRequestDto.setCountryCode(COUNTRY_CODE);
        mobileNoRequestDto.setLocalNumber(LOCAL_NUMBER);
        userMobileUpdateRequestDto.setUserId(USER_ID);
        userMobileUpdateRequestDto.setMobileNo(mobileNoRequestDto);

        when(userRepository.findById(USER_ID)).thenThrow(new
                AuthServiceException("Updating users to database was failed"));

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> {
            userService.updateMobileNo(userMobileUpdateRequestDto);
        });
        assertEquals("Updating users to database was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_UpdatingMobileForInvalidUserId() {
        UserMobileUpdateRequestDto userMobileUpdateRequestDto = new UserMobileUpdateRequestDto();
        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
        mobileNoRequestDto.setCountryCode(COUNTRY_CODE);
        mobileNoRequestDto.setLocalNumber(LOCAL_NUMBER);
        userMobileUpdateRequestDto.setUserId(USER_ID);
        userMobileUpdateRequestDto.setMobileNo(mobileNoRequestDto);

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.empty());
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            userService.updateMobileNo(userMobileUpdateRequestDto);
        });
        assertEquals("Invalid user id", exception.getMessage());
    }


    /**
     * Update password
     */

    @Test
    void Should_Update_Password_When_User_Is_Valid() {

        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = new UserPasswordUpdateRequestDto();
        userPasswordUpdateRequestDto.setUserId(USER_ID);
        userPasswordUpdateRequestDto.setPassword(USER_PASS);
        userPasswordUpdateRequestDto.setNewPassword(NEW_USER_PASS);

        User user = getUserForPassword();

        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches(user.getPassword(), userPasswordUpdateRequestDto.getPassword())).thenReturn(true);

        userService.updatePassword(userPasswordUpdateRequestDto);

        verify(userRepository).save(user);
    }

    @Test
    void Should_ThrowException_When_UpdatePasswordFailed() {
        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = new UserPasswordUpdateRequestDto();
        userPasswordUpdateRequestDto.setUserId(USER_ID);
        userPasswordUpdateRequestDto.setPassword(USER_PASS);
        userPasswordUpdateRequestDto.setNewPassword(NEW_USER_PASS);
        User user = getUserForPassword();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenThrow(new DataAccessException("failed") {
        });
        when(passwordEncoder.matches(user.getPassword(), userPasswordUpdateRequestDto.getPassword())).thenReturn(true);
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.updatePassword(userPasswordUpdateRequestDto));
        assertEquals("Updating password was failed", exception.getMessage());
    }

    @Test
    void Should_Throw_InvalidPasswordException_When_Password_Does_Not_Match() {

        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = new UserPasswordUpdateRequestDto();
        userPasswordUpdateRequestDto.setUserId(USER_ID);
        userPasswordUpdateRequestDto.setPassword(USER_PASS);
        userPasswordUpdateRequestDto.setNewPassword(NEW_USER_PASS);

        User user = getUserForPassword();

        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(user));

        when(passwordEncoder.matches(user.getPassword(), userPasswordUpdateRequestDto.getPassword())).thenReturn(false);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> {
            userService.updatePassword(userPasswordUpdateRequestDto);
        });

        assertEquals("Updating password was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_UpdatingUserPasswordIsFailed() {
        when(userRepository.save(user)).thenThrow(new DataAccessException("failed") {
        });
        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = new UserPasswordUpdateRequestDto();

        User user = getUserForPassword();

        when(userRepository.findById(userPasswordUpdateRequestDto.getUserId())).thenReturn(java.util.Optional.of(user));

        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.updatePassword(userPasswordUpdateRequestDto));
        assertEquals("Updating password was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_UpdatingUserForNonExistingUser() {
        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto = new UserPasswordUpdateRequestDto();
        when(userRepository.findById(userPasswordUpdateRequestDto.getUserId())).thenThrow(new DataAccessException("failed") {
        });

        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.updatePassword(userPasswordUpdateRequestDto));
        assertEquals("Getting user from database was failed", exception.getMessage());
    }

    @Test
    void Should_ThrowExe() {

        when(userRepository.findById(USER_ID)).thenThrow(new DataAccessException("failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.isFirstTimeUserEmail(USER_ID));
        assertEquals("Getting user from database was failed", exception.getMessage());
    }

    @Test
    void Should_Return_False_When_FirstTimeUserEmailForUserWithEmailAddress() {
        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.ofNullable(user));
        assertFalse(userService.isFirstTimeUserEmail(USER_ID));
    }

    @Test
    void Should_Return_True_When_FirstTimeUserEmailForUserWithEmail() {
        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.ofNullable(user));
        user.setEmail(null);
        assertTrue(userService.isFirstTimeUserEmail(USER_ID));
    }
//
//    /*
//     * Reset Password
//     */
//    @Test
//    void Should_ThrowException_When_PasswordResetToken_Is_Expired() {
//        ResetPasswordRequestDto resetPasswordRequestDto = new ResetPasswordRequestDto();
//        ReflectionTestUtils.setField(resetPasswordRequestDto, "password", "password");
//        ReflectionTestUtils.setField(resetPasswordRequestDto, "token", PASSWORD_RESET_TOKEN);
//        when(passwordResetTokenRepository.findByToken(resetPasswordRequestDto.getToken()))
//                .thenReturn(getPasswordResetToken());
//        ExpiredPasswordResetTokenException exception = assertThrows(ExpiredPasswordResetTokenException.class, () ->
//                userService.resetPassword(resetPasswordRequestDto));
//        assertEquals("Password reset token was expired", exception.getMessage());
//    }

    private PasswordResetToken getPasswordResetToken() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setExpiration(1);
        passwordResetToken.setId("uid-1");
        passwordResetToken.setExpiryDate(new Date());
        passwordResetToken.setToken(PASSWORD_RESET_TOKEN);
        passwordResetToken.setUser(getSampleUser());
        return passwordResetToken;
    }

    @Test
    void Should_ThrowException_When_ResetPasswordFailed() {
        ResetPasswordRequestDto resetPasswordRequestDto = new ResetPasswordRequestDto();
        ReflectionTestUtils.setField(resetPasswordRequestDto, "password", "password");
        ReflectionTestUtils.setField(resetPasswordRequestDto, "token", PASSWORD_RESET_TOKEN);
        when(passwordResetTokenRepository.findByToken(resetPasswordRequestDto.getToken()))
                .thenThrow(new DataAccessException("failed") {
                });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.resetPassword(resetPasswordRequestDto));
        assertEquals("Reset password was failed", exception.getMessage());
    }

    /*
     * Get token by userId
     */
    @Test
    void Should_ReturnToken_When_UserId() {
        Collection<OAuth2AccessToken> tokensByUserName = new ArrayList<>();
        tokensByUserName.add(oAuth2AccessToken);
        when(jdbcTokenStore.findTokensByUserName(USER_ID)).thenReturn(tokensByUserName);
        assertEquals(tokensByUserName, userService.getTokenByUserId(USER_ID));
    }

    @Test
    void Should_ThrowException_When_GettingTokenByUserIdFailed() {
        when(jdbcTokenStore.findTokensByUserName(USER_ID))
                .thenThrow(new DataAccessException("Failed") {
                });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.getTokenByUserId(USER_ID));
        assertEquals("Getting token from database was failed", exception.getMessage());
    }

    /**
     * Save updated mobile no
     */

    @Test
    void Should_SaveUpdateMobileNumber() {
        when(userMobileRepository.save(userMobileNo)).thenReturn(userMobileNo);
        userService.addUpdatedNumber(userMobileNo);
        verify(userMobileRepository).save(userMobileNo);
    }

    @Test
    void Should_ThrowException_When_SaveUpdateMobileNumberFailed() {
        when(userMobileRepository.save(userMobileNo)).thenThrow(new DataAccessException("failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.addUpdatedNumber(userMobileNo));
        assertEquals("Adding mobile number to user mobile table failed", exception.getMessage());
    }

    /**
     * Get updated mobile no list
     */
    @Test
    void Should_Return_UpdatedMobileNoList() {
        Pageable pageable2 = PageRequest.of(0, 250, Sort.Direction.ASC, "createdAt");
        PageImpl<UserMobileNo> userMobileNos = new PageImpl<>(Collections.singletonList(userMobileNo), pageable2, 2);
        when(userMobileRepository.findAll(pageable2)).thenReturn(userMobileNos);
        doNothing().when(userMobileRepository).deleteAll(userMobileNos);
        userService.getUpdateMobileNoList(pageable2);
        verify(userMobileRepository).findAll(pageable2);
    }

    @Test
    void Should_ThrowException_When_Return_UpdatedMobileNoListFailed() {
        Pageable pageable = PageRequest.of(0, 250, Sort.Direction.ASC, "createdAt");
        when(userMobileRepository.findAll(pageable)).thenThrow(new DataAccessException("failed") {
        });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.getUpdateMobileNoList(pageable));
        assertEquals("Getting updated mobile numbers failed", exception.getMessage());
    }

    /**
     * Get bulk users
     */
//    @Test
//    void Should_Return_UserList_When_getBulkUsersSuccess() {
//        when(userRepository.findAllById(userId())).thenReturn(getUserList());
//        userService.getUserList(userId());
//        verify(userRepository).findAllById(userId());
//    }
//
//    @Test
//    void Should_ThrowException_When_Return_getBulkUsersFailed() {
//        when(userRepository.findAllById(userId())).thenThrow(new DataAccessException("failed") {
//        });
//        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
//                userService.getUserList(userId()));
//        assertEquals("Error getting user list", exception.getMessage());
//    }

//    @Test
//    void Should_ThrowException_When_GetDefaultRoleFailed() {
//        when(roleRepository.findByName("USER")).thenThrow(new DataAccessException("failed") {
//        });
//        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
//                userService.getDefaultRole());
//        assertEquals("Failed to get role", exception.getMessage());
//    }
    @Test
    void Should_ThrowException_When_GetRegisteredUserFailed() {
        when(userRepository.findByMobileNoOrEmailAndIsRegisteredUserTrue(USER_NAME, USER_NAME))
                .thenThrow(new DataAccessException("failed") {
                });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () ->
                userService.getRegisteredUser(USER_NAME));
        assertEquals("Getting user from database was failed", exception.getMessage());
    }


    private UserMobileNo getUserMobileNo() {
        UserMobileUpdateRequestDto userMobileUpdateRequestDto = new UserMobileUpdateRequestDto();
        userMobileUpdateRequestDto.setMobileNo(getMobileNoRequestDto());
        return new UserMobileNo(userMobileUpdateRequestDto.getMobileNo().getNo(), USER_ID);
    }

    private MobileNoRequestDto getMobileNoRequestDto() {
        MobileNoRequestDto mobileNoRequestDto = new MobileNoRequestDto();
        mobileNoRequestDto.setCountryCode("+62");
        mobileNoRequestDto.setLocalNumber("81123123");
        return mobileNoRequestDto;
    }

    private UserUpdateRequestDto getSampleUserUpdateRequestDto() {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setFullName(USER_NAME);
        return userUpdateRequestDto;
    }

    private User getUserForPassword() {
        User user = new User();
        user.setId("uid-1");
        user.setFullName("user1");
        user.setEmail("user@gmail.com");
        user.setMobileNo("+661323324");
        user.setImageUrl("");
        user.setLanguage(Language.valueOf("ENGLISH"));
        user.setPassword(USER_PASS);
        user.setEnabled(true);
        user.setRegisteredUser(true);
        return user;
    }


    private User getSampleUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setFullName("user1");
        user.setEmail(USER_EMAIL);
        user.setMobileNo(USER_MOBILE_NO);
        user.setRole(new Role(RoleType.USER));
        user.setImageUrl("");
        user.setLanguage(Language.valueOf("ENGLISH"));
        user.setEnabled(true);
        user.setRegisteredUser(true);
        return user;
    }

    private List<String> userId() {
        List<String> userIds = new ArrayList<>();
        userIds.add("uid-1");
        userIds.add("uid-2");
        userIds.add("uid-3");
        return userIds;
    }

    private List<User> getUserList() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId("uid-1");
        user.setFullName("user-1");
        user.setImageUrl("http://s3-test/fid123");
        User user2 = new User();
        user2.setId("uid-2");
        user2.setFullName("user-2");
        user2.setImageUrl("http://s3-test/fid4567");
        users.add(user);
        users.add(user);
        return users;
    }
}