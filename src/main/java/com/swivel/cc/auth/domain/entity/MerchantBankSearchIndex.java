package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "merchant_bank_search_index")
@Getter
@Setter
@NoArgsConstructor
public class MerchantBankSearchIndex {

    @Id
    private String userId;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus merchantApprovalStatus;
    private Date joinedOn;
    private String imageUrl;
    private String businessId;
    private String businessName;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus businessApprovalStatus;
    private String businessImageUrl;
    @Enumerated(EnumType.STRING)
    private RoleType userRole;

    public MerchantBankSearchIndex(User user) {
        this.userId = user.getId();
        this.fullName = user.getFullName();
        this.merchantApprovalStatus = user.getApprovalStatus();
        this.joinedOn = user.getCreatedAt();
        this.imageUrl = user.getImageUrl();
        this.userRole = RoleType.valueOf(user.getRole().getName());
    }

    /**
     * This method is used to update merchant business profile.
     *
     * @param business business
     */
    public void setBusiness(Business business) {
        this.businessId = business.getId();
        this.businessName = business.getBusinessName();
        this.businessApprovalStatus = business.getApprovalStatus();
        this.businessImageUrl = business.getImageUrl();
    }

    /**
     * This method is used to update merchant personal profile.
     *
     * @param merchant merchant
     */
    public void updateMerchant(User merchant) {
        this.fullName = merchant.getFullName();
        this.merchantApprovalStatus = merchant.getApprovalStatus();
        this.imageUrl = merchant.getImageUrl();
    }
}
