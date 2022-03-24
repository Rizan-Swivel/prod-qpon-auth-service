package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.UserMobileNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMobileRepository extends JpaRepository<UserMobileNo, String> {
}
