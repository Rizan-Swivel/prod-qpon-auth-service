package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<User, String> {

    /**
     * This method returns user based on mobileNo or email
     *
     * @param mobileNo mobile number
     * @param email    email
     * @return user/null
     */
    Optional<User> findByMobileNoAsUserNameOrEmail(String mobileNo, String email);

}
