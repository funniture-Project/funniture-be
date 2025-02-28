package com.ohgiraffers.funniture.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true) // toBuilder 추가
public class MemberAndPointEntity {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "email")
    private String email;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "signup_date")
    private LocalDateTime signupDate;

    @Column(name = "member_role")
    private String memberRole;

    @Transient
    private int currentPoint;

    @Transient
    private int is_rejected;

    // 25-02-24, 사용자 기본 주소 추가
//    @Column(name = "address")
//    private String address;
}
