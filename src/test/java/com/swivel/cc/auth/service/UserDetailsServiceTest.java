package com.swivel.cc.auth.service;

import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.repository.UserDetailRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UserDetailsServiceTest {

    private static final String USER_EMAIL = "rajith@tokoin.io";
    private static final String USER_MOBILE_NO = "+6281123123";

    @Mock
    private UserDetailRepository userDetailRepository;
    @Mock
    private UserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userDetailService = new UserDetailService(userDetailRepository);
    }

    @AfterEach
    void tearDown() {}

    @Test
    void Should_ThrowException_When_UserNotFound() {

        when(userDetailRepository.findByMobileNoAsUserNameOrEmail(USER_MOBILE_NO, USER_EMAIL))
                .thenReturn(java.util.Optional.of(new User()));
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailService.loadUserByUsername(USER_EMAIL));
        assertEquals("Invalid username or Password", exception.getMessage());
    }

}
