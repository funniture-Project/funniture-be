package com.ohgiraffers.funniture.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohgiraffers.funniture.member.entity.MemberAndPointEntity;

import lombok.*;


import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberAndPointDTO {

    private String memberId;

    private String email;

    private String userName;

    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime signupDate;

    private String memberRole;

    private PointDTO pointDTO;

    // 포인트 가져오려고 매핑시키기
    public MemberAndPointDTO(MemberAndPointEntity entity) {
        this.memberId = entity.getMemberId();
        this.email = entity.getEmail();
        this.userName = entity.getUserName();
        this.phoneNumber = entity.getPhoneNumber();
        this.signupDate = entity.getSignupDate();
        this.memberRole = entity.getMemberRole();
        this.pointDTO = new PointDTO(entity.getCurrentPoint()); // 포인트 DTO 매핑
    }
}

