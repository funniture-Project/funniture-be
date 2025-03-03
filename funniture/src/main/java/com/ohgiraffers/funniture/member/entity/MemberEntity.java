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
    private int isConsulting;

    // hasImage에 null도 들어가 있을 때 조회하면 에러 발생하므로
    // nullable = true 추가하였고, Integer로 변경 (250218)
    @Column (name = "has_image", nullable = true)
    private Integer hasImage;

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
        return new MemberEntity(memberId, email,password,userName,phoneNumber,signupDate,memberRole,isConsulting,hasImage,imageId,imageLink,address, reasonRejection);
    }

//    // 연관관계 설정 (한 명의 회원이 여러 개의 상품을 소유할 수 있음)
//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
//    private List<ProductEntity> products = new ArrayList<>();
//
//    // 연관관계 설정 (한 명의 회원이 여러 개의 문의를 작성할 수 있음)
//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
//    private List<InquiryEntity> inquiries = new ArrayList<>();
}
