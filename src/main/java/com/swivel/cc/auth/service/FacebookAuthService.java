package com.swivel.cc.auth.service;


import com.swivel.cc.auth.domain.TokenResponse;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.domain.response.SocialSignInResponseDto;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.exception.NoUserException;
import com.swivel.cc.auth.exception.UserAlreadyExistsException;
import com.swivel.cc.auth.repository.UserRepository;
import com.swivel.cc.auth.wrapper.FacebookResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
public class FacebookAuthService {

    public static final String USER_NOT_FOUND = "Invalid user";
    private static final String VALIDATE_URL = "https://graph.facebook.com/v8.0/me?fields=id,name,email&access_token=##ACCESS_TOKEN##";
    private static final String ACCESS_TOKEN_KEY = "##ACCESS_TOKEN##";
    private static final String USER_ID_HEADER = "User-Id";
    private static final String MOBILE_NO_KEY = "{mobileNo}";
    private static final String FAILED_GET_USER_BY_MOBILE_NO = "Getting user by mobile no was failed from auth service";
    private static final String FAILED_GET_USER_BY_ID = "Getting user by id was failed from auth service";
    private static final String FAILED_CREATE_USER = "Creating user was failed from auth service";
    private static final String FAILED_LOG_MESSAGE = "{}. statusCode: {}, body: {}";
    private static final int SUCCESS_STATUS_CODE = HttpStatus.OK.value();
    private static final int BAD_REQUEST_CODE = HttpStatus.BAD_REQUEST.value();
    private final RestTemplate restTemplate = new RestTemplate();
    private final CustomTokenService customTokenService;
    private final UserRepository userRepository;
    private final UserService userService;

    public FacebookAuthService(CustomTokenService customTokenService,
                               UserRepository userRepository, UserService userService) {
        this.customTokenService = customTokenService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

//    public GoogleUser getUser(String userId) {
//        try {
//            Optional<GoogleUser> userOptional = googleUserRepository.findById(userId);
//            if (userOptional.isPresent()) {
//                return userOptional.get();
//            } else {
//                throw new InvalidUserException("Invalid user.");
//            }
//        } catch (DataAccessException e) {
//            throw new AuthServiceException("Getting user by id was failed", e);
//        }
//    }

//        public GoogleUser updateUser(UserUpdateRequestDto userUpdateRequestDto, String userId) {
//
//        try {
//            GoogleUser userByUserId = getUser(userId);
//            userByUserId.setFullName(userUpdateRequestDto.getFullName().trim());
//            String imageUrl = userUpdateRequestDto.getImageUrl();
//            MobileNoRequestDto mobileNo = userUpdateRequestDto.getMobileNo();
//            if (userUpdateRequestDto.getLanguage() != null) {
//                userByUserId.setLanguage(Language.getLanguage(userUpdateRequestDto.getLanguage()));
//            }
//            userByUserId.setImageUrl(imageUrl == null ? null : imageUrl.trim());
//            userByUserId.setMobileNo(mobileNo == null ? null : mobileNo.getNo());
////            userByUserId.setUpdatedAt(new Date());
//            googleUserRepository.save(userByUserId);
//
//            return userByUserId;
//
//        } catch (DataAccessException e) {
//            throw new AuthServiceException("Updating user was failed.", e);
//        }
//    }


    public User createUser(String accessToken) {
        FacebookResponseWrapper facebookResponseWrapper = validate(accessToken);
        User user = new User(facebookResponseWrapper);
        try {
            boolean emailExist = userService.isEmailExist(facebookResponseWrapper.getEmail());
            if (!emailExist) {
                user.setRole(userService.getDefaultRole());
                userRepository.save(user);
                return user;
            } else {
                throw new UserAlreadyExistsException("Already existing user.");
            }
        } catch (DataAccessException e) {
            throw new AuthServiceException("Checking email existence was failed", e);
        }
    }

    public SocialSignInResponseDto loginUser(String accessToken) {
        FacebookResponseWrapper googleResponseWrapper = validate(accessToken);
        try {
            User user = userRepository.findByEmail(googleResponseWrapper.getEmail());
            if (user == null) {
                throw new NoUserException(USER_NOT_FOUND);
            } else {
                TokenResponse tokenResponse = customTokenService.generateToken(user);
                return new SocialSignInResponseDto(user, tokenResponse);
            }
        } catch (DataAccessException e) {
            throw new AuthServiceException("Checking email existence was failed", e);
        }
    }

//    public void logoutUser(String userId, String accessToken) {
//        try {
//            Optional<GoogleUser> userOptional = googleUserRepository.findById(userId);
//            if (userOptional.isPresent()) {
//                GoogleUser user = userOptional.get();
//                String token = accessToken.split(" ")[1];
//                if (user.getAccessToken() != null && user.getAccessToken().equals(token)) {
//                    user.cleanTokens();
//                    googleUserRepository.save(user);
//                } else {
//                    throw new InvalidUserException("Invalid user");
//                }
//            } else {
//                throw new InvalidUserException("Invalid user");
//            }
//        } catch (DataAccessException e) {
//            throw new AuthServiceException("Checking email existence was failed", e);
//        }
//    }


    public FacebookResponseWrapper validate(String accessToken) {

        String url = VALIDATE_URL.replace(ACCESS_TOKEN_KEY, accessToken);
        HttpHeaders headers = getAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            log.debug("Calling google service to validate user. url: {}", url);

            ResponseEntity<FacebookResponseWrapper> result =
                    restTemplate.exchange(url, HttpMethod.GET, entity, FacebookResponseWrapper.class);
            log.debug("Getting user by mobile no was successful. statusCode: {}, response: {}",
                    result.getStatusCode(), result.getBody().toLogJson());
            return result.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == BAD_REQUEST_CODE) {
                throw new InvalidUserException("Invalid user token", e);
            } else {
                log.error(FAILED_LOG_MESSAGE, FAILED_GET_USER_BY_MOBILE_NO, e.getRawStatusCode(), e.getResponseBodyAsString());
                throw new AuthServiceException(FAILED_GET_USER_BY_MOBILE_NO, e);
            }

        }

    }

    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


}
