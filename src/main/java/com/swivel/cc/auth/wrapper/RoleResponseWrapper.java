package com.swivel.cc.auth.wrapper;

import com.swivel.cc.auth.domain.ResourcePermission;
import com.swivel.cc.auth.domain.response.ResponseDto;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseWrapper implements ResponseDto, Serializable {

    private int id;
    private String name;
    private List<ResourcePermission> permissions;

    @Override
    public String toLogJson() {
        return toJson();
    }

}


