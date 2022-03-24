package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.BlockedMerchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Blocked merchant repository
 */
@Repository
public interface BlockedMerchantRepository extends JpaRepository<BlockedMerchant, String> {
}
