package com.swivel.cc.auth.configuration;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * CustomLoginException
 */
@JsonSerialize(using = CustomInvalidLoginExceptionSerializer.class)
public class CustomLoginException extends OAuth2Exception {

    public CustomLoginException(String msg) {
        super(msg);
    }
}
