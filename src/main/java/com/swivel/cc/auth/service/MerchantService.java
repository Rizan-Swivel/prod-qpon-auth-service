package com.swivel.cc.auth.service;

import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.domain.BusinessProfile;
import com.swivel.cc.auth.domain.entity.*;
import com.swivel.cc.auth.domain.request.BusinessRequestDto;
import com.swivel.cc.auth.domain.request.ContactRequestDto;
import com.swivel.cc.auth.domain.request.MerchantStatusUpdateRequestDto;
import com.swivel.cc.auth.domain.request.MobileNoRequestDto;
import com.swivel.cc.auth.domain.response.PendingContactResponseDto;
import com.swivel.cc.auth.enums.*;
import com.swivel.cc.auth.exception.*;
import com.swivel.cc.auth.repository.*;
import com.swivel.cc.auth.service.factories.MerchantProfileRepositoryFactory;
import com.swivel.cc.auth.service.factories.MerchantStatusRepositoryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.swivel.cc.auth.enums.ApprovalStatus.*;

/**
 * Merchant Service
 */
@Service
@Slf4j
public class MerchantService {

    private static final String ALL = "ALL";
    private static final String CREATED_AT = "createdAt";
    private static final String UPDATED_AT = "updatedAt";
    private static final String INVALID_ACTION = "Invalid action";
    private static final String EMAIL_GREETING_PREFIX = "<MERCHANT-NAME>";
    private static final String INVALID_MERCHANT_ID = "Invalid merchant id";
    private static final String INVALID_BANK_ID = "Invalid bank id";
    private final BankBusinessRepository bankBusinessRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final RejectedProfileUpdatesRepository rejectedProfileUpdatesRepository;
    private final NotificationService notificationService;
    private final ApprovedBusinessRepository approvedBusinessRepository;
    private final ApprovedBankBusinessRepository approvedBankBusinessRepository;
    private final MerchantBankSearchIndexRepository merchantBankSearchIndexRepository;
    private final BlockedMerchantRepository blockedMerchantRepository;
    NotificationMetaData notificationMetaData;
    @Autowired
    Translator translator;

    @Autowired
    public MerchantService(NotificationService notificationService,
                           MerchantBankSearchIndexRepository merchantBankSearchIndexRepository,
                           MerchantStatusRepositoryFactory merchantStatusRepositoryFactory,
                           MerchantProfileRepositoryFactory merchantProfileRepositoryFactory,
                           ApprovedBankBusinessRepository approvedBankBusinessRepository) {

        this.notificationService = notificationService;
        this.merchantBankSearchIndexRepository = merchantBankSearchIndexRepository;
        this.userRepository = (UserRepository) merchantProfileRepositoryFactory.getRepository(UserProfileType.GENERIC);
        this.businessRepository =
                (BusinessRepository) merchantProfileRepositoryFactory.getRepository(UserProfileType.BUSINESS);
        this.bankBusinessRepository =
                (BankBusinessRepository) merchantProfileRepositoryFactory.getRepository(UserProfileType.BANK_BUSINESS);
        this.contactRepository =
                (ContactRepository) merchantProfileRepositoryFactory.getRepository(UserProfileType.CONTACT);
        this.approvedBusinessRepository =
                (ApprovedBusinessRepository) merchantStatusRepositoryFactory.getRepository(APPROVED);
        this.rejectedProfileUpdatesRepository =
                (RejectedProfileUpdatesRepository) merchantStatusRepositoryFactory.getRepository(REJECTED);
        this.blockedMerchantRepository =
                (BlockedMerchantRepository) merchantStatusRepositoryFactory.getRepository(BLOCKED);
        this.approvedBankBusinessRepository = approvedBankBusinessRepository;
    }

    /**
     * Method to check ApprovalStatus & ApprovalAction combination.
     *
     * @param existingStatus existingApprovalStatus
     * @param action         approvalAction
     * @return Approval status action
     */
    private ApprovalStatusAction checkApprovalStatusAndAction(String existingStatus, String action) {
        String checkCombinationString = existingStatus + "_" + action;
        try {
            return ApprovalStatusAction.valueOf(checkCombinationString);
        } catch (IllegalArgumentException e) {
            throw new InvalidActionException(INVALID_ACTION);
        }
    }

    /**
     * This method will update approval status for merchant.
     *
     * @param merchantStatusUpdateRequestDto merchantStatusUpdateRequest
     */
    public void updateMerchantApprovalStatus(MerchantStatusUpdateRequestDto merchantStatusUpdateRequestDto,
                                             String timeZone) {
        try {

            Optional<User> optionalMerchant = userRepository.findById(merchantStatusUpdateRequestDto.getMerchantId());
            String action = merchantStatusUpdateRequestDto.getAction().toString();
            if (optionalMerchant.isPresent()) {
                User merchant = optionalMerchant.get();
                String existingStatus = merchant.getApprovalStatus().toString();
                ApprovalStatusAction approvalStatusAction = checkApprovalStatusAndAction(existingStatus, action);

                switch (approvalStatusAction) {
                    case BLOCKED_UNBLOCK:
                        merchant.setApprovalStatus(UNBLOCKED);
                        this.notificationMetaData = new NotificationMetaData(UNBLOCKED, merchant, timeZone);
                        break;
                    case UNBLOCKED_BLOCK:
                    case APPROVED_BLOCK:
                        merchant.setApprovalStatus(BLOCKED);
                        addCommentForBlockedMerchant(merchantStatusUpdateRequestDto);
                        this.notificationMetaData = new NotificationMetaData(BLOCKED, merchant, timeZone);
                        break;
                    case PENDING_APPROVE:
                        merchant.setApprovalStatus(APPROVED);
                        this.notificationMetaData = new NotificationMetaData(APPROVED, merchant, timeZone);
                        break;
                    default:
                        throw new InvalidActionException("Invalid approval status.");
                }
                merchant.setUpdatedAt(new Date());
                userRepository.save(merchant);
                sendNotification(notificationMetaData);
            } else {
                throw new InvalidUserException("Invalid user id");
            }
        } catch (DataAccessException e) {
            throw new AuthServiceException("Retrieving data failed for approval status update", e);
        }
    }

    /**
     * This method will return pending merchants.
     *
     * @param page       page
     * @param size       size
     * @param searchTerm searchTerm
     * @return pending merchant page
     */
    public Page<User> getPendingMerchant(int page, int size, String searchTerm) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, CREATED_AT);
            Page<User> pendingMerchantPage;
            if (searchTerm.equals(ALL)) {
                pendingMerchantPage = userRepository.findByRoleIdAndApprovalStatus(3,
                        PENDING, pageable);
            } else {
                pendingMerchantPage = userRepository.findByApprovalStatusAndFullNameContaining(ApprovalStatus.PENDING,
                        searchTerm, pageable);
            }
            return pendingMerchantPage;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get merchant", e);
        }
    }

    /**
     * This method is used to get pending business information with searchTerm.
     *
     * @param page       page
     * @param size       size
     * @param searchTerm searchTerm
     * @return page of business
     */
    public Page<Business> getPendingBusinessInfoList(int page, int size, String searchTerm) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, UPDATED_AT);
            Page<Business> businessInfoPage;
            if (searchTerm.equals(ALL)) {
                businessInfoPage = businessRepository.findByApprovalStatus(PENDING, pageable);
            } else {
                businessInfoPage = businessRepository.
                        findByApprovalStatusAndBusinessNameContaining(PENDING, searchTerm, pageable);
            }
            return businessInfoPage;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get pending business info from database.", e);
        }
    }

    /**
     * This method is used to get pending bank business information with searchTerm.
     *
     * @param page       page
     * @param size       size
     * @param searchTerm searchTerm
     * @return page of bank business
     */
    public Page<BankBusiness> getPendingBankBusinessInfoList(int page, int size, String searchTerm) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, UPDATED_AT);
            Page<BankBusiness> businessInfoPage;
            if (searchTerm.equals(ALL)) {
                businessInfoPage = bankBusinessRepository.findByApprovalStatus(PENDING, pageable);
            } else {
                businessInfoPage = bankBusinessRepository.
                        findByApprovalStatusAndBusinessNameContaining(PENDING, searchTerm, pageable);
            }
            return businessInfoPage;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get pending bank business info from database.", e);
        }
    }

    /**
     * This method is used to get pending contact information with searchTerm.
     *
     * @param page       page
     * @param size       size
     * @param searchTerm searchTerm
     * @return page of contact
     */
    public Page<Contact> getPendingContactInfoList(int page, int size, String searchTerm, RoleType roleType) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, UPDATED_AT);
            Page<Contact> contactInfoPage;
            if (searchTerm.equals(ALL)) {
                contactInfoPage = contactRepository.findByApprovalStatusAndRoleType(PENDING, roleType, pageable);
            } else {
                contactInfoPage = contactRepository.findByApprovalStatusAndRoleTypeAndNameContaining(PENDING, roleType,
                        searchTerm, pageable);
            }
            return contactInfoPage;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get pending contact info from database.", e);
        }
    }

    /**
     * This method is used to get business information for contacts.
     *
     * @param contactInfoPage contactInfoPage
     * @param roleType        MERCHANT/BANK
     * @param timeZone        timeZone
     * @return pending contact list with business info.
     */
    public List<PendingContactResponseDto> getBusinessDetailsForContact(Page<Contact> contactInfoPage, String roleType,
                                                                        String timeZone) {
        List<PendingContactResponseDto> responseDtoList = new ArrayList<>();
        for (Contact contact : contactInfoPage) {
            Optional<Business> optionalBusiness =
                    getLatestBusinessProfileByMerchantId(contact.getMerchant().getId(), roleType);
            optionalBusiness.ifPresent(business -> responseDtoList.add(
                    new PendingContactResponseDto(contact, timeZone, business)));
        }
        return responseDtoList;
    }

    /**
     * This method saves business info in database.
     *
     * @param business business
     */
    public void saveBusiness(Business business, RoleType roleType) {
        try {
            if (roleType == RoleType.MERCHANT) {
                businessRepository.save(business);
            } else {
                bankBusinessRepository.save(new BankBusiness(business));
            }
            Optional<MerchantBankSearchIndex> merchantSearchIndex =
                    merchantBankSearchIndexRepository.findById(business.getMerchant().getId());
            if (merchantSearchIndex.isPresent()) {
                merchantSearchIndex.get().setBusiness(business);
                merchantBankSearchIndexRepository.save(merchantSearchIndex.get());
            }
        } catch (DataAccessException e) {
            throw new AuthServiceException("Saving business data to database was failed", e);
        }
    }

    /**
     * This method is used to get the latest business profile with userId.
     *
     * @param userId userId
     * @return latest business profile.
     */
    public Optional<Business> getLatestBusinessInfoByUserId(String userId, RoleType roleType) {
        try {
            Optional<Business> business;
            if (roleType.equals(RoleType.MERCHANT)) {
                business = businessRepository.getLatestBusinessByMerchantId(userId);
            } else {
                Optional<BankBusiness> optionalBankBusiness = bankBusinessRepository.getLatestBusinessByBankId(userId);
                business = optionalBankBusiness.map(Business::new);
            }
            return business;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get latest business profile from database.", e);
        }
    }

    /**
     * This method is used to get pending business profile with userId.
     *
     * @param userId   userId
     * @param roleType Merchant/Bank
     * @return pending business profile.
     */
    private Optional<Business> getPendingBusinessInfoByUserId(String userId, RoleType roleType) {
        try {
            Optional<Business> optionalBusiness;
            if (roleType.equals(RoleType.MERCHANT))
                optionalBusiness = businessRepository.findByApprovalStatusAndMerchantId(ApprovalStatus.PENDING, userId);
            else {
                Optional<BankBusiness> optionalBankBusiness =
                        bankBusinessRepository.findByApprovalStatusAndBankId(ApprovalStatus.PENDING, userId);
                optionalBusiness = optionalBankBusiness.map(Business::new);
            }
            return optionalBusiness;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get pending business profile from database.", e);
        }
    }

    /**
     * This method is used to get latest approved business profile with userId.
     *
     * @param userId   userId
     * @param roleType roleType
     * @return existing business info
     */
    public Business getApprovedBusinessInfoByUserId(String userId, RoleType roleType) {
        try {
            Business business;
            if (roleType.equals(RoleType.MERCHANT)) {
                Optional<ApprovedBusiness> approvedBusiness = approvedBusinessRepository.findByMerchantId(userId);
                business = approvedBusiness.map(ApprovedBusiness::getBusiness).orElse(null);
            } else {
                Optional<ApprovedBankBusiness> approvedBankBusiness = approvedBankBusinessRepository.findByBankId(userId);
                business = approvedBankBusiness.map(bankBusiness -> new Business(bankBusiness.getBankBusiness()))
                        .orElse(null);
            }
            return business;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get latest approved business profile from database.", e);
        }
    }

    /**
     * This method is used to get merchant business profile with businessId.
     *
     * @param businessId businessId
     * @param roleType   roleType
     * @return existing business info
     */
    public Business getBusinessInfoByBusinessId(String businessId, RoleType roleType) {
        try {
            Optional<Business> business;
            if (roleType.equals(RoleType.MERCHANT)) {
                business = businessRepository.findById(businessId);
            } else {
                Optional<BankBusiness> optionalBankBusiness = bankBusinessRepository.findById(businessId);
                business = optionalBankBusiness.map(Business::new);
            }

            if (business.isPresent()) {
                return business.get();
            } else {
                throw new BusinessProfileException("Invalid business id");
            }
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get business profile from database.", e);
        }
    }

    /**
     * This method saves contact info in database.
     *
     * @param contact contact
     */
    public void saveContact(Contact contact) {
        try {
            contactRepository.save(contact);
        } catch (DataAccessException e) {
            throw new AuthServiceException("Saving contact data to database was failed", e);
        }
    }

    /**
     * This method will check whether merchant exist in user table.
     *
     * @param merchantId merchantId
     */
    public void validateMerchantId(String merchantId) {
        Optional<User> user = userRepository.findById(merchantId);
        if (user.isEmpty()) {
            throw new InvalidUserException(INVALID_MERCHANT_ID);
        }
    }

    /**
     * This method will get merchant's contact data with merchantId.
     *
     * @param merchantId merchantId
     * @return existing contact info
     */
    private Optional<Contact> getPendingContactInfoByMerchantId(String merchantId) {
        try {
            return contactRepository.getPendingContactByMerchantId(merchantId);
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get merchant contact info from database.", e);
        }
    }

    /**
     * This method will get merchant's latest updated contact data with merchantId.
     *
     * @param merchantId merchantId
     * @return approved contact info
     */
    public Optional<Contact> getContactInfoByMerchantId(String merchantId) {
        try {
            return contactRepository.getLastUpdatedContactByMerchantId(merchantId);
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get merchant contact info from database.", e);
        }
    }

    /**
     * This method creates or update contact information
     *
     * @param contactRequestDto contactRequestDto
     * @return contact
     */
    public Contact createOrUpdateContact(ContactRequestDto contactRequestDto, RoleType roleType) {
        User user = getBusinessProfileUserById(contactRequestDto.getMerchantId());
        if (!user.getRole().getName().equals(roleType.toString()))
            throw new InvalidUserException("URL roleType didn't match with user roleType");
        var existingPendingContact = getPendingContactInfoByMerchantId(contactRequestDto.getMerchantId());
        Contact contact;
        if (existingPendingContact.isPresent()) {
            existingPendingContact.get().update(contactRequestDto);
            contact = existingPendingContact.get();
        } else {
            contact = new Contact(contactRequestDto, user, roleType);
        }
        saveContact(contact);
        return contact;
    }

    /**
     * This method will return business profile user from user table.
     *
     * @param userId userId
     * @return business profile user
     */
    private User getBusinessProfileUserById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty() || (user.get().getRole().getId() != RoleType.MERCHANT.getId()
                && user.get().getRole().getId() != RoleType.BANK.getId())) {
            throw new InvalidUserException("Invalid user id");
        } else {
            return user.get();
        }
    }

    /**
     * This method creates or update business
     *
     * @param businessRequestDto businessRequestDto
     * @return business profile
     */
    public Business createOrUpdateBusiness(BusinessRequestDto businessRequestDto, RoleType toUserType) {
        User user = getBusinessProfileUserById(businessRequestDto.getMerchantId());
        if (!toUserType.toString().equals(user.getRole().getName()))
            throw new InvalidUserException("Invalid user id for user role.");

        Optional<Business> existingPendingBusiness = getPendingBusinessInfoByUserId(user.getId(), toUserType);
        Business business;
        if (existingPendingBusiness.isPresent()) {
            existingPendingBusiness.get().update(businessRequestDto);
            business = existingPendingBusiness.get();
        } else {
            business = new Business(businessRequestDto, user);
        }
        saveBusiness(business, toUserType);
        return business;
    }

    /**
     * This method is used update merchant's business profile approval status.
     *
     * @param rejectedProfileUpdates rejectedProfileUpdates
     * @param approvalAction         approvalAction
     * @param timeZone               timeZone
     * @param roleType               MERCHANT/BANK
     */
    @Transactional
    public void updateMerchantBusinessApprovalStatus(RejectedProfileUpdates rejectedProfileUpdates,
                                                     ApprovalAction approvalAction, String timeZone, RoleType roleType) {

        try {
            Optional<Business> optionalBusiness;
            if (roleType.equals(RoleType.MERCHANT)) {
                optionalBusiness = businessRepository.findById(rejectedProfileUpdates.getReferenceId());
            } else {
                Optional<BankBusiness> optionalBankBusiness =
                        bankBusinessRepository.findById(rejectedProfileUpdates.getReferenceId());
                optionalBusiness = optionalBankBusiness.map(Business::new);
            }
            if (optionalBusiness.isPresent()) {
                approveOrRejectBusinessProfile(roleType, approvalAction, timeZone,
                        optionalBusiness.get(), rejectedProfileUpdates);
            } else {
                throw new InvalidUserException("Invalid business id");
            }
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to update business approval status", e);
        }
    }

    /**
     * This method is used to approve/reject business profile.
     *
     * @param roleType               MERCHANT/BANK
     * @param approvalAction         approvalAction
     * @param timeZone               timeZone
     * @param business               business profile
     * @param rejectedProfileUpdates rejectedProfileUpdates
     */
    private void approveOrRejectBusinessProfile(RoleType roleType, ApprovalAction approvalAction, String timeZone,
                                                Business business, RejectedProfileUpdates rejectedProfileUpdates) {

        User businessMerchant = business.getMerchant();
        ApprovalStatus existingStatus = business.getApprovalStatus();
        if (existingStatus == PENDING && approvalAction == ApprovalAction.APPROVE) {
            business.setApprovalStatus(APPROVED);
            if (roleType.equals(RoleType.MERCHANT)) {
                approvedBusinessRepository.deleteByMerchantId(business.getMerchant().getId());
                approvedBusinessRepository.save(new ApprovedBusiness(business));
            } else {
                approvedBankBusinessRepository.deleteByBankId(business.getMerchant().getId());
                approvedBankBusinessRepository.save(new ApprovedBankBusiness(business));
            }
            this.notificationMetaData = new NotificationMetaData(APPROVED, businessMerchant, timeZone);

        } else if (existingStatus == PENDING && approvalAction == ApprovalAction.REJECT) {
            business.setApprovalStatus(REJECTED);
            rejectedProfileUpdates.setRoleType(roleType);
            rejectedProfileUpdates.setMerchantInfoType(MerchantInfo.BUSINESS);
            rejectedProfileUpdatesRepository.save(rejectedProfileUpdates);
            this.notificationMetaData = new NotificationMetaData(REJECTED, businessMerchant, timeZone);

        } else {
            throw new InvalidActionException(INVALID_ACTION);
        }
        business.setUpdatedAt(new Date());
        saveBusiness(business, roleType);
        sendNotification(notificationMetaData);
    }

    /**
     * This method is used update merchant's contact profile approval status.
     *
     * @param rejectedProfileUpdates rejectedProfileUpdates
     * @param approvalAction         approvalAction
     */
    public void updateMerchantContactApprovalStatus(RejectedProfileUpdates rejectedProfileUpdates,
                                                    ApprovalAction approvalAction, String timeZone) {

        try {
            Optional<Contact> optionalContact = contactRepository.findById(rejectedProfileUpdates.getReferenceId());
            if (optionalContact.isPresent()) {
                Contact contact = optionalContact.get();
                User contactMerchant = contact.getMerchant();
                ApprovalStatus existingStatus = contact.getApprovalStatus();
                if (existingStatus == PENDING && approvalAction == ApprovalAction.APPROVE) {
                    contact.setApprovalStatus(APPROVED);
                    this.notificationMetaData = new NotificationMetaData(APPROVED, contactMerchant, timeZone);
                } else if (existingStatus == PENDING && approvalAction == ApprovalAction.REJECT) {
                    contact.setApprovalStatus(REJECTED);
                    rejectedProfileUpdates.setMerchantInfoType(MerchantInfo.CONTACT);
                    rejectedProfileUpdatesRepository.save(rejectedProfileUpdates);
                    this.notificationMetaData = new NotificationMetaData(REJECTED, contactMerchant, timeZone);
                } else {
                    throw new InvalidActionException(INVALID_ACTION);
                }
                contact.setUpdatedAt(new Date());
                contactRepository.save(contact);
                sendNotification(notificationMetaData);
            } else {
                throw new InvalidUserException("Invalid contact id");
            }
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to update contact approval status", e);
        }
    }

    /**
     * This method will get merchant's contact data with contactId.
     *
     * @param contactId contactId
     * @return existing contact info
     */
    public Contact getContactInfoByContactId(String contactId) {
        try {
            Optional<Contact> contact = contactRepository.findById(contactId);
            if (contact.isPresent()) {
                return contact.get();
            } else {
                throw new ContactProfileException("Invalid contact id");
            }
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get contact profile from database.", e);
        }
    }

    /**
     * This method is used to get bulk merchant/ bank information.
     *
     * @param merchantIds merchantIds
     * @return merchant list.
     */
    public List<Business> getBulkMerchantList(List<String> merchantIds, RoleType userType) {
        try {
            List<Business> businessList = new ArrayList<>();
            if (RoleType.MERCHANT.equals(userType)) {
                for (String merchantId : merchantIds) {
                    Optional<ApprovedBusiness> approvedBusiness = approvedBusinessRepository.findByMerchantId(merchantId);
                    approvedBusiness.ifPresent(business -> businessList.add(business.getBusiness()));
                }
            } else {
                Optional<List<ApprovedBankBusiness>> approvedBankBusinesses =
                        approvedBankBusinessRepository.findAllByBankIdIn(merchantIds);
                if (approvedBankBusinesses.isPresent()) {
                    approvedBankBusinesses.get().forEach(approvedBankBusiness ->
                            businessList.add(new Business(approvedBankBusiness.getBankBusiness())));
                }
            }
            return businessList;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Error getting bulk merchant list", e);
        }
    }


    /**
     * This method generate sms, email
     *
     * @param notificationMetaData notificationMetaData
     */
    private void sendNotification(NotificationMetaData notificationMetaData) {

        String sms = translator.toLocale(notificationMetaData.getApprovalStatus().getSms());
        var mobileNoRequestDto = new MobileNoRequestDto(notificationMetaData.getMerchant().getMobileNo());
        var timeZone = notificationMetaData.getTimeZone();
        notificationService.sendSms(mobileNoRequestDto, sms, timeZone);
        if (notificationMetaData.getMerchant().getEmail() != null &&
                !notificationMetaData.getMerchant().getEmail().isEmpty()) {
            String subject = translator.toLocale(notificationMetaData.getApprovalStatus().getEmailSubject());
            String bodyTemplate = translator.toLocale(notificationMetaData.getApprovalStatus().getEmailBody());
            String body = bodyTemplate.replace(EMAIL_GREETING_PREFIX, notificationMetaData.getMerchant().getFullName());
            String merchantEmail = notificationMetaData.getMerchant().getEmail();
            notificationService.sendMail(merchantEmail, subject, body, timeZone);
        }
    }

    /**
     * This method will check whether merchant exist in business table.
     *
     * @param merchantId merchantId
     * @param roleType   roleType
     */
    public void validateBusinessProfileWithMerchantId(String merchantId, RoleType roleType) {
        if (roleType.equals(RoleType.MERCHANT)) {
            Optional<ApprovedBusiness> approvedBusiness = approvedBusinessRepository.findByMerchantId(merchantId);
            if (approvedBusiness.isEmpty())
                throw new InvalidUserException(INVALID_MERCHANT_ID);
        } else {
            Optional<ApprovedBankBusiness> approvedBankBusiness = approvedBankBusinessRepository.findByBankId(merchantId);
            if (approvedBankBusiness.isEmpty())
                throw new InvalidUserException(INVALID_BANK_ID);
        }
    }

    /**
     * This method is used to get merchant business profile details at login.
     *
     * @param merchantId merchantId
     * @return merchant business profile details.
     */
    public BusinessProfile getMerchantOrBankLoginResponse(String merchantId, String roleType) {
        BusinessProfile businessProfile = new BusinessProfile();
        Optional<Business> business = getLatestBusinessProfileByMerchantId(merchantId, roleType);
        if (business.isEmpty()) {
            businessProfile.setUpdated(false);
        } else {
            businessProfile.setUpdated(true);
            businessProfile.setApprovalStatus(business.get().getApprovalStatus());
        }
        return businessProfile;
    }

    /**
     * This method fetch the latest business profile with merchant id.
     *
     * @param merchantId merchantId
     * @return business profile.
     */
    public Optional<Business> getLatestBusinessProfileByMerchantId(String merchantId, String roleType) {
        try {
            Optional<Business> business;
            if (roleType.equals(RoleType.MERCHANT.toString())) {
                Optional<ApprovedBusiness> approvedBusiness = approvedBusinessRepository.findByMerchantId(merchantId);
                business = approvedBusiness.map(ApprovedBusiness::getBusiness).or(() ->
                        businessRepository.getLatestBusinessByMerchantId(merchantId));
            } else {
                Optional<ApprovedBankBusiness> approvedBankBusiness = approvedBankBusinessRepository.findByBankId(merchantId);
                Optional<BankBusiness> bankBusiness = approvedBankBusiness.map(ApprovedBankBusiness::getBankBusiness)
                        .or(() -> bankBusinessRepository.getLatestBusinessByBankId(merchantId));
                business = bankBusiness.map(Business::new);
            }
            return business;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get latest business profile from database", e);
        }
    }

    /**
     * This method is used to check merchant is active or not.
     * Merchant is active when approval status is approved/unblocked and should have an approved business profile.
     *
     * @param merchantId merchantId
     * @return true/false
     */
    public boolean isMerchantOrBankActive(String merchantId) {
        try {
            Optional<User> merchant = userRepository.findById(merchantId);
            if (merchant.isPresent()) {
                ApprovalStatus userApprovalStatus = merchant.get().getApprovalStatus();
                String userRole = merchant.get().getRole().getName();
                if ((userApprovalStatus == APPROVED || userApprovalStatus == UNBLOCKED) &&
                        userRole.equals(RoleType.MERCHANT.toString())) {
                    Optional<ApprovedBusiness> approvedBusiness =
                            approvedBusinessRepository.findByMerchantId(merchantId);
                    return approvedBusiness.isPresent();
                }
                if ((userApprovalStatus == APPROVED || userApprovalStatus == UNBLOCKED) &&
                        userRole.equals(RoleType.BANK.toString())) {
                    Optional<ApprovedBankBusiness> approvedBankBusiness =
                            approvedBankBusinessRepository.findByBankId(merchantId);
                    return approvedBankBusiness.isPresent();
                }
            }
            return false;
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to read user/approvedBusiness from the db to verify isActive for merchant.", e);
        }
    }

    /**
     * This method is used to get active merchants count.
     *
     * @return active merchant count.
     */
    public int getActiveMerchantCount() {
        try {
            return approvedBusinessRepository.getActiveMerchantCount();
        } catch (DataAccessException e) {
            throw new AuthServiceException("Failed to get active merchant count from the db.", e);
        }
    }

    /**
     * This method will add comment for blocked merchant.
     *
     * @param merchantStatusUpdateRequestDto RequestDto
     */
    private void addCommentForBlockedMerchant(MerchantStatusUpdateRequestDto merchantStatusUpdateRequestDto) {
        try {
            BlockedMerchant blockedMerchant = new BlockedMerchant(merchantStatusUpdateRequestDto);
            blockedMerchantRepository.save(blockedMerchant);
        } catch (DataAccessException e) {
            throw new AuthServiceException("Saving blocked-merchant comment to database was failed", e);
        }
    }
}
