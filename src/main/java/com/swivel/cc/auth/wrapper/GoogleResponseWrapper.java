package com.swivel.cc.auth.wrapper;

import com.swivel.cc.auth.domain.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class GoogleResponseWrapper implements BaseDto {

    private String name;
    private String email;
    private String picture;
    private String locale;
    private boolean email_verified;

    @Override
    public String toLogJson() {
        return toJson();
    }
}
