package com.swivel.cc.auth.domain.entity;

import com.swivel.cc.auth.domain.request.UnregisterUserDto;
import com.swivel.cc.auth.domain.request.UserRequestDto;
import com.swivel.cc.auth.domain.request.UserUpdateRequestDto;
import com.swivel.cc.auth.enums.ApprovalStatus;
import com.swivel.cc.auth.enums.Language;
import com.swivel.cc.auth.wrapper.FacebookResponseWrapper;
import com.swivel.cc.auth.wrapper.GoogleResponseWrapper;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "user")
public class User implements Serializable {

    @Transient
    private static final String USER_ID_PREFIX = "uid-";
    @Transient
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Id
    private String id;
    @Column(name = "fullname")
    private String fullName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "mobileNo")
    private String mobileNo;
    @Column(name = "mobileNoAsUserName")
    private String mobileNoAsUserName;
    @Column(name = "imageUrl")
    private String imageUrl;
    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;
    @Column(name = "approvalStatus")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @Column(name = "createdAt")
    private Date createdAt;
    @Column(name = "updatedAt")
    private Date updatedAt;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "is_registered")
    private boolean isRegisteredUser;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.mobileNo = user.getMobileNo();
        this.mobileNoAsUserName = user.getMobileNoAsUserName();
        this.imageUrl = user.getImageUrl();
        this.language = user.getLanguage();
        this.approvalStatus = user.getApprovalStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.enabled = user.isEnabled();
        this.isRegisteredUser = user.isRegisteredUser();
        this.role = user.getRole();
    }

    public User(GoogleResponseWrapper googleResponseWrapper) {
        this.id = USER_ID_PREFIX + UUID.randomUUID();
        this.fullName = googleResponseWrapper.getName();
        this.email = googleResponseWrapper.getEmail();
        this.imageUrl = googleResponseWrapper.getPicture();
        this.language = googleResponseWrapper.getLocale().equalsIgnoreCase("en") ?
                Language.ENGLISH : null;
        this.isRegisteredUser = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.enabled = googleResponseWrapper.isEmail_verified();

    }


    public User(UserRequestDto userRequestDto) {
        this.id = USER_ID_PREFIX + UUID.randomUUID();
        this.fullName = userRequestDto.getFullName().trim();
        this.email = userRequestDto.getEmail() != null && !userRequestDto.getEmail().isEmpty() ?
                userRequestDto.getEmail().trim() : null;
        this.mobileNo = userRequestDto.getMobileNo().getNo();
        this.mobileNoAsUserName = userRequestDto.getMobileNo().getNo().replace("-", "");
        this.password = bCryptPasswordEncoder.encode(userRequestDto.getPassword());
        this.language = Language.getLanguage(userRequestDto.getLanguage());
        this.role = new Role(userRequestDto.getRoleType());
        this.isRegisteredUser = true;
        this.enabled = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.approvalStatus = ApprovalStatus.APPROVED;
    }

    public User(UnregisterUserDto unregisterUserDto) {
        this.id = USER_ID_PREFIX + UUID.randomUUID();
        this.mobileNo = unregisterUserDto.getMobileNo().getNo();
        this.language = Language.getLanguage(unregisterUserDto.getLanguage());
        this.isRegisteredUser = false;
        this.enabled = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public User(FacebookResponseWrapper facebookResponseWrapper) {
        this.id = USER_ID_PREFIX + facebookResponseWrapper.getId();
        this.fullName = facebookResponseWrapper.getName();
        this.email = facebookResponseWrapper.getEmail();
        this.language = Language.ENGLISH;
        this.isRegisteredUser = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public User(UserUpdateRequestDto userUpdateRequestDto) {
        this.fullName = userUpdateRequestDto.getFullName();
    }

}
