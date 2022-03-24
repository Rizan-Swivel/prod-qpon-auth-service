package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.domain.request.MerchantStatusUpdateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Rejected merchant entity
 */
@Entity
@Table(name = "rejected_merchants")
@Getter
@Setter
@NoArgsConstructor
public class RejectedMerchant {

    @Transient
    private static final String REJECTED_MERCHANT_ID_PREFIX = "rmid-";
    @Id
    @Column(nullable = false)
    private String id;
    private String merchantId;
    private String comment;
    private Date createdAt;

    public RejectedMerchant(MerchantStatusUpdateRequestDto merchantStatusUpdateRequestDto) {
        this.id = REJECTED_MERCHANT_ID_PREFIX + UUID.randomUUID();
        this.merchantId = merchantStatusUpdateRequestDto.getMerchantId();
        this.comment = merchantStatusUpdateRequestDto.getComment();
        this.createdAt = new Date();
    }
}
