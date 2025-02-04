package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberDTO {

    private String memberId;      // 사용자회원번호

    private String ownerId;      // 회원번호

    private String email;         // 이메일

    private String ownerEmail;    // 제공자 이메일

    private String ownerName;    // 제공자 이름

    private String ownerPhone;    // 제공자 휴대전화번호

    private String password;      // 비밀번호

    private String userName;      // 이름

    private String phoneNumber;   // 휴대전화번호

    private Date signupDate;      // 회원가입일

    private String memberRole;    // 회원 권한

    private Boolean isConsulting; // 상담 여부

    private String imageLink;     // 이미지 링크

    private String imageId;       // 이미지 ID
}
