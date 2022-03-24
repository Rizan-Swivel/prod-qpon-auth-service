package com.swivel.cc.auth.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SmsRequestDto extends RequestDto {

    private final String message;
    private MobileNoRequestDto recipientNo;

    @Override
    public String toLogJson() {
        return toJson();
    }
}
