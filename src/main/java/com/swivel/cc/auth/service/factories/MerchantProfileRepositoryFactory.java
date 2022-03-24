package com.swivel.cc.auth.service.factories;

import com.swivel.cc.auth.enums.UserProfileType;
import com.swivel.cc.auth.exception.InvalidActionException;
import com.swivel.cc.auth.repository.BankBusinessRepository;
import com.swivel.cc.auth.repository.BusinessRepository;
import com.swivel.cc.auth.repository.ContactRepository;
import com.swivel.cc.auth.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MerchantProfileRepositoryFactory {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final BankBusinessRepository bankBusinessRepository;

    public MerchantProfileRepositoryFactory(ContactRepository contactRepository, UserRepository userRepository,
                                            BusinessRepository businessRepository,
                                            BankBusinessRepository bankBusinessRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.bankBusinessRepository = bankBusinessRepository;
    }

    public JpaRepository getRepository(UserProfileType userProfileType) {
        switch (userProfileType) {
            case GENERIC:
                return userRepository;
            case BUSINESS:
                return businessRepository;
            case BANK_BUSINESS:
                return bankBusinessRepository;
            case CONTACT:
                return contactRepository;
            default:
                throw new InvalidActionException("Invalid user profile type: " + userProfileType);
        }
    }
}
