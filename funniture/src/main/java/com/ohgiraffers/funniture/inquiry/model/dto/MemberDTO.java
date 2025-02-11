package com.ohgiraffers.funniture.inquiry.model.dto;

import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberDTO {

    private String memberId;

    private String memberRole;

    private String email;

    private String password;

    private String userName;

    private String phoneNumber;

    private LocalDateTime signupDate;

    private int isConsulting;

    private String imageLink;

    private String imageId;

}