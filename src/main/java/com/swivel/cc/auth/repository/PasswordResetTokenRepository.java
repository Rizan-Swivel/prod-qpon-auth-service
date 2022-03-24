package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    /**
     * Return the PasswordResetToken by token
     *
     * @return PasswordResetToken
     */
    PasswordResetToken findByToken(String token);

    /**
     * Return the password reset token by user Id
     *
     * @param userId userId
     * @return passwordRestToken
     */
    PasswordResetToken findByUserId(String userId);

}
