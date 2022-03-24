package com.swivel.cc.auth.service;

import com.swivel.cc.auth.domain.AuthUserDetail;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.repository.UserDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserDetails Service
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailService implements UserDetailsService {

    private final UserDetailRepository userDetailRepository;

    @Autowired
    public UserDetailService(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) {

        Optional<User> user = userDetailRepository.findByMobileNoAsUserNameOrEmail(name, name);

        if (user.isEmpty()) {
            user = userDetailRepository.findById(name);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("Invalid username or Password");
            }
        }
        return new AuthUserDetail(user.get());
    }
}
