package com.swivel.cc.auth.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "approved_bank_business")
@Setter
@Getter
@NoArgsConstructor
public class ApprovedBankBusiness {
    @Id
    private String id;
    @OneToOne
    @JoinColumn
    private BankBusiness bankBusiness;
    private String businessName;
    private String ownerName;
    private String telephone;
    private String email;
    private String bankId;

    public ApprovedBankBusiness(Business business) {
        this.id = business.getId();
        this.bankBusiness = new BankBusiness(business);
        this.businessName = business.getBusinessName();
        this.ownerName = business.getOwnerName();
        this.telephone = business.getTelephone();
        this.email = business.getEmail();
        this.bankId = business.getMerchant().getId();
    }
}
