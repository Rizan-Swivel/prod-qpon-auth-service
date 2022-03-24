package com.swivel.cc.auth.service;

import com.swivel.cc.auth.domain.entity.Business;
import com.swivel.cc.auth.domain.entity.Role;
import com.swivel.cc.auth.domain.entity.User;
import com.swivel.cc.auth.domain.request.BusinessRequestDto;
import com.swivel.cc.auth.domain.request.MobileNoRequestDto;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.RoleType;
import com.swivel.cc.auth.exception.AuthServiceException;
import com.swivel.cc.auth.exception.InvalidUserException;
import com.swivel.cc.auth.repository.ApprovedBankBusinessRepository;
import com.swivel.cc.auth.repository.BusinessRepository;
import com.swivel.cc.auth.repository.MerchantBankSearchIndexRepository;
import com.swivel.cc.auth.repository.UserRepository;
import com.swivel.cc.auth.service.factories.MerchantProfileRepositoryFactory;
import com.swivel.cc.auth.service.factories.MerchantStatusRepositoryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests the {@link MerchantService } class.
 */
class MerchantServiceTest {

    private static final String MERCHANT_ID = "uid-bd0c8894-8ddb-4b1d-8383-de44bfaa297f";

    private MerchantService merchantService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private MerchantBankSearchIndexRepository merchantBankSearchIndexRepository;
    @Mock
    private MerchantStatusRepositoryFactory merchantStatusRepositoryFactory;
    @Mock
    private MerchantProfileRepositoryFactory merchantProfileRepositoryFactory;
    @Mock
    private ApprovedBankBusinessRepository approvedBankBusinessRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
        merchantService = new MerchantService(notificationService, merchantBankSearchIndexRepository,
                merchantStatusRepositoryFactory, merchantProfileRepositoryFactory, approvedBankBusinessRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Should_CreateOrUpdateBusiness_WhenBusinessRequestDtoIsGivenWithAllFieldsForExistingBusiness() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();
        businessRequestDto.setMerchantId(MERCHANT_ID);
        businessRequestDto.setBusinessName("Singer");
        businessRequestDto.setOwnerName("Owner");
        businessRequestDto.setMobileNo(new MobileNoRequestDto("+94", "713321911"));
        businessRequestDto.setTelephone(new MobileNoRequestDto("+94", "72993391"));
        businessRequestDto.setImageUrl("https://www.singersl.com/image.jpg");
        businessRequestDto.setEmail("singerlk@gmail.com");
        businessRequestDto.setBusinessRegNo("SL123");
        businessRequestDto.setAddress("N0.20, Station road, Dehiwala.");
        businessRequestDto.setWebSite("https://www.singersl.com/");
        businessRequestDto.setFacebook("https://www.facebook.com/SingerSL/");
        businessRequestDto.setInstagram("https://www.instagram.com/singer_srilanka/?hl=en");

        User merchant = new User();
        merchant.setId(MERCHANT_ID);
        merchant.setRole(new Role(RoleType.MERCHANT));

        Business existingBusiness = new Business();
        existingBusiness.setId("bisid-123");
        existingBusiness.setMerchant(merchant);

        when(userRepository.findById(MERCHANT_ID)).thenReturn(Optional.of(merchant));
        when(businessRepository.findByApprovalStatusAndMerchantId(ApprovalStatus.PENDING, MERCHANT_ID))
                .thenReturn(Optional.of(existingBusiness));

        Business business = merchantService.createOrUpdateBusiness(businessRequestDto, RoleType.MERCHANT);
        verify(businessRepository).save(business);
        assertEquals(MERCHANT_ID, business.getMerchant().getId());
        assertEquals("Singer", business.getBusinessName());
        assertEquals("Owner", business.getOwnerName());
        assertEquals("+94-72993391", business.getTelephone());
        assertEquals("https://www.singersl.com/image.jpg", business.getImageUrl());
        assertEquals("singerlk@gmail.com", business.getEmail());
        assertEquals("SL123", business.getBusinessRegNo());
        assertEquals("N0.20, Station road, Dehiwala.", business.getAddress());
        assertEquals("https://www.singersl.com/", business.getWebSite());
        assertEquals("https://www.facebook.com/SingerSL/", business.getFacebook());
        assertEquals("https://www.instagram.com/singer_srilanka/?hl=en", business.getInstagram());
    }

    @Test
    void Should_CreateOrUpdateBusiness_WhenBusinessRequestDtoIsGivenWithoutOptionalFieldsForExistingBusiness() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();
        businessRequestDto.setMerchantId(MERCHANT_ID);
        businessRequestDto.setBusinessName("Singer");
        businessRequestDto.setOwnerName("Owner");
        businessRequestDto.setMobileNo(new MobileNoRequestDto("+94", "713321911"));
        businessRequestDto.setTelephone(new MobileNoRequestDto("+94", "72993391"));
        businessRequestDto.setImageUrl("https://www.singersl.com/image.jpg");

        User merchant = new User();
        merchant.setId(MERCHANT_ID);
        merchant.setRole(new Role(RoleType.MERCHANT));

        Business existingBusiness = new Business();
        existingBusiness.setId("bisid-123");
        existingBusiness.setMerchant(merchant);

        when(userRepository.findById(MERCHANT_ID)).thenReturn(Optional.of(merchant));
        when(businessRepository.findByApprovalStatusAndMerchantId(ApprovalStatus.PENDING, MERCHANT_ID))
                .thenReturn(Optional.of(existingBusiness));

        Business business = merchantService.createOrUpdateBusiness(businessRequestDto, RoleType.MERCHANT);
        verify(businessRepository).save(business);
        assertEquals(MERCHANT_ID, business.getMerchant().getId());
        assertEquals("Singer", business.getBusinessName());
        assertEquals("Owner", business.getOwnerName());
        assertEquals("+94-72993391", business.getTelephone());
        assertEquals("https://www.singersl.com/image.jpg", business.getImageUrl());
    }

    @Test
    void Should_CreateOrUpdateBusiness_WhenBusinessRequestDtoIsGivenWithAllFieldsForNewBusiness() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();
        businessRequestDto.setMerchantId(MERCHANT_ID);
        businessRequestDto.setBusinessName("Singer");
        businessRequestDto.setOwnerName("Owner");
        businessRequestDto.setMobileNo(new MobileNoRequestDto("+94", "713321911"));
        businessRequestDto.setTelephone(new MobileNoRequestDto("+94", "72993391"));
        businessRequestDto.setImageUrl("https://www.singersl.com/image.jpg");
        businessRequestDto.setEmail("singerlk@gmail.com");
        businessRequestDto.setBusinessRegNo("SL123");
        businessRequestDto.setAddress("N0.20, Station road, Dehiwala.");
        businessRequestDto.setWebSite("https://www.singersl.com/");
        businessRequestDto.setFacebook("https://www.facebook.com/SingerSL/");
        businessRequestDto.setInstagram("https://www.instagram.com/singer_srilanka/?hl=en");

        User merchant = new User();
        merchant.setId(MERCHANT_ID);
        merchant.setRole(new Role(RoleType.MERCHANT));

        when(userRepository.findById(MERCHANT_ID)).thenReturn(Optional.of(merchant));
        when(businessRepository.findByApprovalStatusAndMerchantId(ApprovalStatus.PENDING, MERCHANT_ID))
                .thenReturn(Optional.empty());

        Business business = merchantService.createOrUpdateBusiness(businessRequestDto, RoleType.MERCHANT);
        verify(businessRepository).save(business);
        assertEquals(MERCHANT_ID, business.getMerchant().getId());
        assertEquals("Singer", business.getBusinessName());
        assertEquals("Owner", business.getOwnerName());
        assertEquals("+94-72993391", business.getTelephone());
        assertEquals("https://www.singersl.com/image.jpg", business.getImageUrl());
        assertEquals("singerlk@gmail.com", business.getEmail());
        assertEquals("SL123", business.getBusinessRegNo());
        assertEquals("N0.20, Station road, Dehiwala.", business.getAddress());
        assertEquals("https://www.singersl.com/", business.getWebSite());
        assertEquals("https://www.facebook.com/SingerSL/", business.getFacebook());
        assertEquals("https://www.instagram.com/singer_srilanka/?hl=en", business.getInstagram());
    }

    @Test
    void Should_CreateOrUpdateBusiness_WhenBusinessRequestDtoIsGivenWithoutOptionalFieldsForNewBusiness() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();
        businessRequestDto.setMerchantId(MERCHANT_ID);
        businessRequestDto.setBusinessName("Singer");
        businessRequestDto.setOwnerName("Owner");
        businessRequestDto.setMobileNo(new MobileNoRequestDto("+94", "713321911"));
        businessRequestDto.setTelephone(new MobileNoRequestDto("+94", "72993391"));
        businessRequestDto.setImageUrl("https://www.singersl.com/image.jpg");

        User merchant = new User();
        merchant.setId(MERCHANT_ID);
        merchant.setRole(new Role(RoleType.MERCHANT));

        when(userRepository.findById(MERCHANT_ID)).thenReturn(Optional.of(merchant));
        when(businessRepository.findByApprovalStatusAndMerchantId(ApprovalStatus.PENDING, MERCHANT_ID))
                .thenReturn(Optional.empty());

        Business business = merchantService.createOrUpdateBusiness(businessRequestDto, RoleType.MERCHANT);
        verify(businessRepository).save(business);
        assertEquals(MERCHANT_ID, business.getMerchant().getId());
        assertEquals("Singer", business.getBusinessName());
        assertEquals("Owner", business.getOwnerName());
        assertEquals("+94-72993391", business.getTelephone());
        assertEquals("https://www.singersl.com/image.jpg", business.getImageUrl());
    }

    @Test
    void Should_ThrowException_When_CreatingOrUpdatingBusinessForInvalidUserId() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();

        when(userRepository.findById(businessRequestDto.getMerchantId())).thenReturn(Optional.empty());
        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            merchantService.createOrUpdateBusiness(businessRequestDto, RoleType.MERCHANT);
        });
        assertEquals("Invalid user id", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_CreatingOrUpdatingBusinessFailedWhileGettingPendingBusinessProfiles() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();

        User merchant = new User();
        merchant.setId(MERCHANT_ID);
        merchant.setRole(new Role(RoleType.MERCHANT));

        when(userRepository.findById(businessRequestDto.getMerchantId())).thenReturn(Optional.of(merchant));
        when(businessRepository
                .findByApprovalStatusAndMerchantId(ApprovalStatus.PENDING, merchant.getId()))
                .thenThrow(new DataAccessException("Failed") {
                });
        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> {
            merchantService.createOrUpdateBusiness(businessRequestDto, RoleType.MERCHANT);
        });
        assertEquals("Failed to get pending business profile from database.", exception.getMessage());
    }

    @Test
    void Should_ThrowException_When_CreatingOrUpdatingBusinessFailedToSaveOnDatabase() {
        BusinessRequestDto businessRequestDto = new BusinessRequestDto();
        businessRequestDto.setMerchantId(MERCHANT_ID);
        businessRequestDto.setBusinessName("Singer");
        businessRequestDto.setOwnerName("Owner");
        businessRequestDto.setMobileNo(new MobileNoRequestDto("+94", "713321911"));
        businessRequestDto.setTelephone(new MobileNoRequestDto("+94", "72993391"));
        businessRequestDto.setImageUrl("https://www.singersl.com/image.jpg");

        User merchant = new User();
        merchant.setRole(new Role(RoleType.MERCHANT));
        Business existingBusiness = new Business();

        when(userRepository.findById(businessRequestDto.getMerchantId())).thenReturn(Optional.of(merchant));
        when(businessRepository
                .findByApprovalStatusAndMerchantId(ApprovalStatus.PENDING, merchant.getId()))
                .thenReturn(Optional.of(existingBusiness));
        when(businessRepository.save(existingBusiness)).thenThrow(new DataAccessException("Failed") {
        });

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> {
            merchantService.createOrUpdateBusiness(businessRequestDto, RoleType.MERCHANT);
        });
        assertEquals("Saving business data to database was failed", exception.getMessage());
    }
}