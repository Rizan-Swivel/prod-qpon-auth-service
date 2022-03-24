package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "deletable")
    private boolean deletable;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "permission_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private List<Permission> permissions;

    public Role(String roleName) {
        this.name = roleName;
        this.deletable = true;
    }

    public Role(RoleType roleType){
        this.id = roleType.getId();
        this.name = roleType.name();
        this.deletable = false;
    }
}