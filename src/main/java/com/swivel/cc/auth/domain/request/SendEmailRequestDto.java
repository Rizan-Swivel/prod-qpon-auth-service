package com.swivel.cc.auth.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendEmailRequestDto extends RequestDto {

    private final String to;
    private final String subject;
    private final String message;

    @Override
    public String toLogJson() {
        return toJson();
    }
}
