package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.RejectedMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Rejected merchant repository
 */
@Repository
public interface RejectedMerchantRepository extends JpaRepository<RejectedMerchant, String> {
}
