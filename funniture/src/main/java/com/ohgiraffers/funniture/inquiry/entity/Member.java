package com.ohgiraffers.funniture.inquiry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "tbl_member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_role")
    private String memberRole;

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

    @Column(name = "is_consulting")
    private int isConsulting;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "image_id")
    private String imageId;

    // 연관관계 설정 (한 명의 회원이 여러 개의 상품을 소유할 수 있음)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    // 연관관계 설정 (한 명의 회원이 여러 개의 문의를 작성할 수 있음)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Inquiry> inquiries = new ArrayList<>();
}
