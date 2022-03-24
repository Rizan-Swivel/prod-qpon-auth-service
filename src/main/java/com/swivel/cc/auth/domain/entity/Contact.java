package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.domain.request.ContactRequestDto;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "contact")
@Getter
@Setter
@NoArgsConstructor
public class Contact {

    @Transient
    private static final String CONTACT_ID_PREFIX = "conid-";
    @Id
    private String id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "merchantId")
    private User merchant;
    private String name;
    private String designation;
    private String telephone;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public Contact(ContactRequestDto contactRequestDto, User user, RoleType roleType) {
        this.id = CONTACT_ID_PREFIX + UUID.randomUUID();
        this.merchant = user;
        this.name = contactRequestDto.getName();
        this.designation = contactRequestDto.getDesignation();
        this.telephone = contactRequestDto.getTelephone().getNo();
        this.email = contactRequestDto.getEmail();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.approvalStatus = ApprovalStatus.PENDING;
        this.roleType = roleType;
    }

    /**
     * Used to update contact information
     *
     * @param contactRequestDto contactRequestDto
     */
    public void update(ContactRequestDto contactRequestDto) {
        this.name = contactRequestDto.getName();
        this.designation = contactRequestDto.getDesignation();
        this.telephone = contactRequestDto.getTelephone().getNo();
        this.email = contactRequestDto.getEmail();
        this.updatedAt = new Date();
    }

}
