package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.domain.request.BusinessRequestDto;
import com.swivel.cc.auth.enums.ApprovalStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "business")
@Getter
@Setter
@NoArgsConstructor
public class Business {

    @Transient
    private static final String BUSINESS_ID_PREFIX = "bisid-";
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "merchantId")
    private User merchant;
    private String businessName;
    private String ownerName;
    private String mobileNo;
    private String telephone;
    private String email;
    private String businessRegNo;
    private String address;
    private String imageUrl;
    private String webSite;
    private String facebook;
    private String instagram;
    private Date createdAt;
    private Date updatedAt;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    public Business(BusinessRequestDto businessRequestDto, User user) {
        this.id = BUSINESS_ID_PREFIX + UUID.randomUUID();
        this.merchant = user;
        this.businessName = businessRequestDto.getBusinessName();
        this.ownerName = businessRequestDto.getOwnerName();
        this.mobileNo = businessRequestDto.getMobileNo().getNo();
        this.telephone = (businessRequestDto.getTelephone() != null)
                ? businessRequestDto.getTelephone().getNo() : null;
        this.email = businessRequestDto.getEmail();
        this.businessRegNo = businessRequestDto.getBusinessRegNo();
        this.address = businessRequestDto.getAddress();
        this.imageUrl = businessRequestDto.getImageUrl();
        this.webSite = businessRequestDto.getWebSite();
        this.facebook = businessRequestDto.getFacebook();
        this.instagram = businessRequestDto.getInstagram();
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public Business(BankBusiness bankBusiness) {
        this.id = bankBusiness.getId();
        this.merchant = bankBusiness.getBank();
        this.businessName = bankBusiness.getBusinessName();
        this.ownerName = bankBusiness.getOwnerName();
        this.mobileNo = bankBusiness.getMobileNo();
        this.telephone = bankBusiness.getTelephone();
        this.email = bankBusiness.getEmail();
        this.businessRegNo = bankBusiness.getBusinessRegNo();
        this.address = bankBusiness.getAddress();
        this.imageUrl = bankBusiness.getImageUrl();
        this.webSite = bankBusiness.getWebSite();
        this.facebook = bankBusiness.getFacebook();
        this.instagram = bankBusiness.getInstagram();
        this.createdAt = bankBusiness.getCreatedAt();
        this.updatedAt = bankBusiness.getUpdatedAt();
        this.approvalStatus = bankBusiness.getApprovalStatus();
    }

    /**
     * Used to update business profile.
     *
     * @param businessRequestDto businessRequestDto
     */
    public void update(BusinessRequestDto businessRequestDto) {
        this.businessName = businessRequestDto.getBusinessName();
        this.ownerName = businessRequestDto.getOwnerName();
        this.mobileNo = businessRequestDto.getMobileNo().getNo();
        this.telephone = (businessRequestDto.getTelephone() != null)
                ? businessRequestDto.getTelephone().getNo() : null;
        this.email = businessRequestDto.getEmail();
        this.businessRegNo = businessRequestDto.getBusinessRegNo();
        this.address = businessRequestDto.getAddress();
        this.imageUrl = businessRequestDto.getImageUrl();
        this.webSite = businessRequestDto.getWebSite();
        this.facebook = businessRequestDto.getFacebook();
        this.instagram = businessRequestDto.getInstagram();
        this.updatedAt = new Date();
    }
}
