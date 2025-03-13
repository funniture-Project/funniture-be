package com.ohgiraffers.funniture.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "tbl_member")
@Getter
@AllArgsConstructor
@Setter
@ToString
public class MemberEntity {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "signup_date")
    private LocalDateTime signupDate;

    @Column(name = "member_role")
    private String memberRole;

    @Column(name = "is_consulting")
    private Boolean isConsulting;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "image_link")
    private String imageLink;

    // 25-02-24, 사용자 기본 주소 추가
    @Column(name = "address")
    private String address;

    @Column (name = "reason_rejection")
    private String reasonRejection;

    protected MemberEntity(){}

    public MemberEntity password(String password){
        this.password = password;
        return this;
    }

    public MemberEntity create(){
        return new MemberEntity(memberId, email,password,userName,phoneNumber,signupDate,memberRole,isConsulting,imageId,imageLink,address, reasonRejection);
    }

}
