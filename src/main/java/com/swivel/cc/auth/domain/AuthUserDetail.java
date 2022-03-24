package com.swivel.cc.auth.domain;

import com.swivel.cc.auth.domain.entity.Role;
import com.swivel.cc.auth.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthUserDetail extends User implements UserDetails {

    public AuthUserDetail(User user) {
        super(user);
    }

    public AuthUserDetail() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Role role = getRole();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        role.getPermissions().forEach(permission ->
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName())));
        return grantedAuthorities;
    }


    @Override
    public String getUsername() {
        return super.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
