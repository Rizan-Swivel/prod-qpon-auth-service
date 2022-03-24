package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.ApprovedBankBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Approved Bank Business Repository
 */
public interface ApprovedBankBusinessRepository extends JpaRepository<ApprovedBankBusiness, String> {

    /**
     * Used to delete approved business by bank id.
     *
     * @param bankId bankId
     */
    void deleteByBankId(String bankId);

    /**
     * Used to get approved business by bank id.
     *
     * @param bankId bankId
     * @return approved business.
     */
    Optional<ApprovedBankBusiness> findByBankId(String bankId);

    /**
     * Used to get list of approvedBankBusiness by bank id list.
     *
     * @param bankIds bank id list
     * @return ApprovedBankBusiness list.
     */
    Optional<List<ApprovedBankBusiness>> findAllByBankIdIn(List<String> bankIds);

    /**
     * Used to get active banks count.
     *
     * @return active banks count.
     */
    @Query("SELECT COUNT(ab) FROM ApprovedBankBusiness ab WHERE ab.bankId IN " +
            " (SELECT u.id FROM User u WHERE u.approvalStatus = 'APPROVED' OR u.approvalStatus = 'UNBLOCKED')")
    int getActiveBankCount();
}
