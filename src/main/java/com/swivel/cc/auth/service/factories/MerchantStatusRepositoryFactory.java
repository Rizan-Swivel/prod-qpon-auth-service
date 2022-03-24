package com.swivel.cc.auth.service.factories;

import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.exception.InvalidActionException;
import com.swivel.cc.auth.repository.ApprovedBusinessRepository;
import com.swivel.cc.auth.repository.BlockedMerchantRepository;
import com.swivel.cc.auth.repository.RejectedProfileUpdatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MerchantStatusRepositoryFactory {

    private final RejectedProfileUpdatesRepository rejectedProfileUpdatesRepository;
    private final ApprovedBusinessRepository approvedBusinessRepository;
    private final BlockedMerchantRepository blockedMerchantRepository;

    @Autowired
    public MerchantStatusRepositoryFactory(RejectedProfileUpdatesRepository rejectedProfileUpdatesRepository,
                                           ApprovedBusinessRepository approvedBusinessRepository,
                                           BlockedMerchantRepository blockedMerchantRepository) {
        this.rejectedProfileUpdatesRepository = rejectedProfileUpdatesRepository;
        this.approvedBusinessRepository = approvedBusinessRepository;
        this.blockedMerchantRepository = blockedMerchantRepository;
    }

    public JpaRepository getRepository(ApprovalStatus approvalStatus) {
        switch (approvalStatus) {
            case APPROVED:
                return approvedBusinessRepository;
            case REJECTED:
                return rejectedProfileUpdatesRepository;
            case BLOCKED:
                return blockedMerchantRepository;
            default:
                throw new InvalidActionException("Unsupported approval status: " + approvalStatus);
        }
    }
}
