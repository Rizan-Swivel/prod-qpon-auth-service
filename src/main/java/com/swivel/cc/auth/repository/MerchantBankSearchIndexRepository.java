package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.MerchantBankSearchIndex;
import com.swivel.cc.auth.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Merchant bank search index repository
 */
public interface MerchantBankSearchIndexRepository extends JpaRepository<MerchantBankSearchIndex, String> {

    /**
     * Get merchant search index by user role, business name & full name.
     *
     * @param searchTerm searchTerm
     * @param userRole   Merchant/Bank
     * @return list of users.
     */
    @Query(value = "SELECT s FROM MerchantBankSearchIndex s WHERE s.userRole = :userRole AND " +
            " (s.businessName LIKE %:searchTerm% OR s.fullName LIKE %:searchTerm%)")
    Page<MerchantBankSearchIndex> getUsersByUserRoleAndSearchTerm(Pageable pageable, String searchTerm,
                                                                  RoleType userRole);

    /**
     * Get all users by user role.
     *
     * @param userRole Merchant/Bank
     * @return list of users.
     */
    Page<MerchantBankSearchIndex> findByUserRole(Pageable pageable, RoleType userRole);
}
