package com.swivel.cc.auth.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swivel.cc.auth.domain.entity.Permission;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * Permission details
 */
@Getter
public class PermissionResponseDto implements ResponseDto {

    @JsonIgnore
    private static final String UNDERSCORE = "_";
    @JsonIgnore
    private static final String SPACE = " ";

    private final int id;
    private final String name;
    private final boolean selected;

    public PermissionResponseDto(Permission permission, boolean selected) {
        this.id = permission.getId();
        this.name = setSentenceCaseName(permission.getName());
        this.selected = selected;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }

    /**
     * Convert the permission name to sentence case
     *
     * @param permissionName permissionName
     * @return string
     */
    private String setSentenceCaseName(String permissionName) {
        String replacedValue = permissionName.replace(UNDERSCORE, SPACE);
        return StringUtils.capitalize(replacedValue);
    }
}
