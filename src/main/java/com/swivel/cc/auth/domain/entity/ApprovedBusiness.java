package com.swivel.cc.auth.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "approved_business")
@Setter
@Getter
@NoArgsConstructor
public class ApprovedBusiness {

    @Id
    private String id;
    @OneToOne
    @JoinColumn
    private Business business;
    private String businessName;
    private String ownerName;
    private String telephone;
    private String email;
    private String merchantId;


    public ApprovedBusiness(Business business) {
        this.id = business.getId();
        this.business = business;
        this.businessName = business.getBusinessName();
        this.ownerName = business.getOwnerName();
        this.telephone = business.getTelephone();
        this.email = business.getEmail();
        this.merchantId = business.getMerchant().getId();
    }
}
