package com.swivel.cc.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ResourcePermission implements Serializable {
    private int resourceId;
    private String resource;
}