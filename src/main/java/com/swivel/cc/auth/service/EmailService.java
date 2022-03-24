package com.swivel.cc.auth.service;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.request.SendEmailRequestDto;
import com.swivel.cc.auth.enums.SendEmailDetails;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.wrapper.SendEmailResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@Slf4j
public class EmailService {

    private static final String REPLACE_STRING = "{token}";
    private final RestTemplate restTemplate;
    private final Translator translator;
    private final String sendEmailUri;
    private final String apiKey;
    private final String link;

    @Autowired
    public EmailService(RestTemplate restTemplate,
                        Translator translator,
                        @Value("${email.password-reset.url}") String sendEmailUri,
                        @Value("${email.password-reset.api-key}") String apiKey,
                        @Value("${email.password-reset.link}") String link) {
        this.restTemplate = restTemplate;
        this.sendEmailUri = sendEmailUri;
        this.apiKey = apiKey;
        this.link = link;
        this.translator = translator;
    }

    /**
     * @param email              email
     * @param passwordResetToken passwordResetToken
     * @return true/false
     */
    public boolean sendEmail(String email, String passwordResetToken) {
        String message = translator.toLocale(SendEmailDetails.PASSWORD_RESET_MSG.getCode()) +
                link.replace(REPLACE_STRING, passwordResetToken);
        HttpHeaders headers = getHeaders();
        SendEmailRequestDto sendEmailRequestDto = new SendEmailRequestDto(email,
                translator.toLocale(SendEmailDetails.SUBJECT.getCode()), message);
        HttpEntity<SendEmailRequestDto> entity = new HttpEntity<>(sendEmailRequestDto, headers);
        try {
            ResponseEntity<SendEmailResponseWrapper> result =
                    restTemplate.exchange(sendEmailUri, HttpMethod.POST, entity, SendEmailResponseWrapper.class);
            log.info("Send email status code : {}", result.getStatusCode());
            return result.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            throw new AuthServiceException("failed", e);
        }

    }

    /**
     * set the headers
     *
     * @return headers
     */
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("app-key", apiKey);
        return headers;
    }


}
