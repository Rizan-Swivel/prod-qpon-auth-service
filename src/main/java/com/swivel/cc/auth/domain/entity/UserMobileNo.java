package com.swivel.cc.auth.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_mobile_no")
@Getter
@Setter
@NoArgsConstructor
public class UserMobileNo {

    @Id
    @Column(name = "user_id")
    private String userId;
    private String mobileNo;
    private Date createdAt;

    public UserMobileNo(String mobileNo, String userId) {
        this.userId = userId;
        this.mobileNo = mobileNo;
        this.createdAt = new Date();
    }
}
