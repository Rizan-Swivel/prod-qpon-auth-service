package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Business repository
 */
public interface BusinessRepository extends JpaRepository<Business, String> {

    /**
     * This method is used to get business page by approval status.
     *
     * @param approvalStatus approvalStatus
     * @param pageable       pageable
     * @return business page
     */
    Page<Business> findByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);

    /**
     * This method is used to get business info by approval status & merchant id.
     *
     * @param approvalStatus approvalStatus
     * @param merchantId     merchantId
     * @return business info
     */
    Optional<Business> findByApprovalStatusAndMerchantId(ApprovalStatus approvalStatus, String merchantId);

    /**
     * This method is used to get business page by approval status & business name.
     *
     * @param approvalStatus approvalStatus
     * @param searchTerm     searchTerm
     * @param pageable       pageable
     * @return business page
     */
    Page<Business> findByApprovalStatusAndBusinessNameContaining(ApprovalStatus approvalStatus,
                                                                 String searchTerm, Pageable pageable);

    /**
     * This method is used to get the latest business by merchant id.
     *
     * @param merchantId merchantId
     * @return business
     */
    @Query(value = "SELECT * FROM business b WHERE b.merchantId = ?1 ORDER BY b.updatedAt DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Business> getLatestBusinessByMerchantId(String merchantId);
}
