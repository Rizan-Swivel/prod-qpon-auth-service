package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.domain.request.MerchantInfoStatusUpdateRequestDto;
import com.swivel.cc.auth.enums.MerchantInfo;
import com.swivel.cc.auth.enums.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "rejected_profile_updates")
@Getter
@Setter
@NoArgsConstructor
public class RejectedProfileUpdates {

    @Transient
    private static final String REJECTED_PROFILE_UPDATES_ID_PREFIX = "rpuid-";
    @Id
    private String id;
    private String referenceId;
    @Enumerated(EnumType.STRING)
    private MerchantInfo merchantInfoType;
    private String comment;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public RejectedProfileUpdates(MerchantInfoStatusUpdateRequestDto merchantInfoStatusUpdateRequestDto) {
        this.id = REJECTED_PROFILE_UPDATES_ID_PREFIX + UUID.randomUUID();
        this.referenceId = merchantInfoStatusUpdateRequestDto.getId();
        this.comment = merchantInfoStatusUpdateRequestDto.getComment();
        this.createdAt = new Date();
    }
}
