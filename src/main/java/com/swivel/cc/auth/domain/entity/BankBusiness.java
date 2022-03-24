package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.domain.request.BusinessRequestDto;
import com.swivel.cc.auth.enums.ApprovalStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bank_business")
@Getter
@Setter
@NoArgsConstructor
public class BankBusiness {
    @Transient
    private static final String BUSINESS_ID_PREFIX = "bisid-";
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "bankId")
    private User bank;
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

    public BankBusiness(Business business) {
        this.id = business.getId();
        this.bank = business.getMerchant();
        this.businessName = business.getBusinessName();
        this.ownerName = business.getOwnerName();
        this.mobileNo = business.getMobileNo();
        this.telephone = business.getTelephone();
        this.email = business.getEmail();
        this.businessRegNo = business.getBusinessRegNo();
        this.address = business.getAddress();
        this.imageUrl = business.getImageUrl();
        this.webSite = business.getWebSite();
        this.facebook = business.getFacebook();
        this.instagram = business.getInstagram();
        this.createdAt = business.getCreatedAt();
        this.updatedAt = business.getUpdatedAt();
        this.approvalStatus = business.getApprovalStatus();
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
