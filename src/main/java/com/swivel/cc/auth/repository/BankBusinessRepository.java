package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.BankBusiness;
import com.swivel.cc.auth.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Bank business repository
 */
public interface BankBusinessRepository extends JpaRepository<BankBusiness, String> {

    /**
     * This method is used to get business info by approval status & bank id.
     *
     * @param approvalStatus approvalStatus
     * @param bankId         bankId
     * @return business info
     */
    Optional<BankBusiness> findByApprovalStatusAndBankId(ApprovalStatus approvalStatus, String bankId);

    /**
     * This method is used to get the latest business by bank id.
     *
     * @param bankId bankId
     * @return business
     */
    @Query(value = "SELECT * FROM bank_business b WHERE b.bankId = ?1 ORDER BY b.updatedAt DESC LIMIT 1",
            nativeQuery = true)
    Optional<BankBusiness> getLatestBusinessByBankId(String bankId);

    /**
     * This method is used to get business page by approval status.
     *
     * @param approvalStatus approvalStatus
     * @param pageable       pageable
     * @return business page
     */
    Page<BankBusiness> findByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);

    /**
     * This method is used to get business page by approval status & business name.
     *
     * @param approvalStatus approvalStatus
     * @param searchTerm     searchTerm
     * @param pageable       pageable
     * @return business page
     */
    Page<BankBusiness> findByApprovalStatusAndBusinessNameContaining(ApprovalStatus approvalStatus,
                                                                     String searchTerm, Pageable pageable);
}
