package com.ohgiraffers.funniture.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table (name = "tbl_member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberEntity {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "signup_date")
    private LocalDateTime signupDate;

    @Column(name = "member_role")
    private String memberRole;

    @Column(name = "is_consulting")
    private int isConsulting;

    @Column (name = "has_image")
    private int hasImage;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "image_link")
    private String imageLink;

    //    // 연관관계 설정 (한 명의 회원이 여러 개의 상품을 소유할 수 있음)
//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
//    private List<ProductEntity> products = new ArrayList<>();
//
//    // 연관관계 설정 (한 명의 회원이 여러 개의 문의를 작성할 수 있음)
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<InquiryEntity> inquiries = new ArrayList<>();

    }