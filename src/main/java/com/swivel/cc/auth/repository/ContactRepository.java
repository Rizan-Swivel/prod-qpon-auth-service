package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.Contact;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, String> {

    /**
     * This method is used to get pending contact info by merchant id.
     *
     * @param merchantId merchantId
     * @return contact info
     */
    @Query(value = "SELECT c FROM Contact c WHERE c.approvalStatus = 'PENDING' AND c.merchant.id = ?1")
    Optional<Contact> getPendingContactByMerchantId(String merchantId);

    /**
     * This method is used to get the latest updated contact by merchant id.
     *
     * @param merchantId merchantId
     * @return approved contact
     */
    @Query(value = "SELECT * FROM contact c WHERE c.merchantId = ?1 ORDER BY" +
            " c.updatedAt DESC LIMIT 1", nativeQuery = true)
    Optional<Contact> getLastUpdatedContactByMerchantId(String merchantId);

    /**
     * This method is used to create contact profiles page by approval status.
     *
     * @param approvalStatus approvalStatus
     * @param pageable       pageable
     * @return page of contact profiles.
     */
    Page<Contact> findByApprovalStatusAndRoleType(ApprovalStatus approvalStatus, RoleType roleType, Pageable pageable);

    /**
     * This method is used to create contact profiles page by approval status & name.
     *
     * @param approvalStatus approvalStatus
     * @param searchTerm     searchTerm
     * @param pageable       pageable
     * @return page of contact profiles.
     */
    Page<Contact> findByApprovalStatusAndRoleTypeAndNameContaining(ApprovalStatus approvalStatus, RoleType roleType,
                                                        String searchTerm, Pageable pageable);
}
