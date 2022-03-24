package com.swivel.cc.auth.domain.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateRoleRequestDto extends RequestDto {

    private String roleName;
    private List<Integer> permissions;

    /**
     * This method checks all required fields are available.
     *
     * @return true/ false
     */
    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(roleName);
    }

    /**
     * This method converts this object to json string for logging purpose.
     * All fields are obfuscated.
     *
     * @return json string
     */
    @Override
    public String toLogJson() {
        return toJson();
    }

}
