package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.domain.request.MerchantStatusUpdateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Blocked merchant entity
 */
@Entity
@Table(name = "blocked_merchants")
@Getter
@Setter
@NoArgsConstructor
public class BlockedMerchant {

    @Transient
    private static final String BLOCKED_MERCHANT_ID_PREFIX = "bmid-";
    @Id
    @Column(nullable = false)
    private String id;
    private String merchantId;
    private String comment;
    private Date createdAt;

    public BlockedMerchant(MerchantStatusUpdateRequestDto merchantStatusUpdateRequestDto) {
        this.id = BLOCKED_MERCHANT_ID_PREFIX + UUID.randomUUID();
        this.merchantId = merchantStatusUpdateRequestDto.getMerchantId();
        this.comment = merchantStatusUpdateRequestDto.getComment();
        this.createdAt = new Date();
    }
}
