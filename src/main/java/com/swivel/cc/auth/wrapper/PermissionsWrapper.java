package com.swivel.cc.auth.wrapper;

import com.swivel.cc.auth.domain.ResourcePermission;
import com.swivel.cc.auth.domain.entity.Permission;
import com.swivel.cc.auth.domain.entity.Resource;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@Data
public class PermissionsWrapper {

    List<ResourcePermission> resourcePermissions = new ArrayList<>();
    HashSet<Resource> resources = new HashSet<>();

    public PermissionsWrapper(List<Permission> permissions) {
        permissions.forEach(permission -> resources.add(permission.getResource()));
        resources.forEach(resource -> resourcePermissions
                .add(new ResourcePermission(resource.getId(), resource.getName())));
    }

    public void attachResources(List<Resource> resources) {
        for (Resource resource : resources) {
            if (resource.isDefault()) {
                resourcePermissions.add(new ResourcePermission(resource.getId(), resource.getName()));
            }
        }
    }
}