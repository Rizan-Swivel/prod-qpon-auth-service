package com.swivel.cc.auth.domain;

import com.swivel.cc.auth.domain.entity.Resource;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.util.ListUtils;
import com.swivel.cc.auth.wrapper.PermissionsWrapper;
import com.swivel.cc.auth.wrapper.RoleResponseWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private RoleResponseWrapper role;

    public TokenResponse(OAuth2AccessToken oAuth2AccessToken, User user, List<Resource> allResources) {
        this.accessToken = oAuth2AccessToken.getValue();
        this.refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
        this.role = attachResources(allResources, user);
    }

    /**
     * Attach the resources
     *
     * @param allResources allResources
     * @param user         user
     * @return roleResponseWrapper
     */
    private RoleResponseWrapper attachResources(List<Resource> allResources, User user) {
        RoleResponseWrapper roleResponseWrapper = new RoleResponseWrapper();
        roleResponseWrapper.setId(user.getRole().getId());
        roleResponseWrapper.setName(user.getRole().getName());
        PermissionsWrapper permissionsWrapper = new PermissionsWrapper(user.getRole().getPermissions());
        permissionsWrapper.attachResources(allResources);
        List<ResourcePermission> collect = permissionsWrapper
                .getResourcePermissions()
                .stream().filter(ListUtils.distinctByKey(ResourcePermission::getResourceId))
                .collect(Collectors.toList());
        roleResponseWrapper.setPermissions(collect);

        return roleResponseWrapper;
    }
}
