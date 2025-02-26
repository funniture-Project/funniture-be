package com.ohgiraffers.funniture.member.entity;

import com.ohgiraffers.funniture.point.entity.PointEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
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

    @Column(name = "current_point") // 추가
    private int currentPoint;

    // 25-02-24, 사용자 기본 주소 추가
//    @Column(name = "address")
//    private String address;
}
