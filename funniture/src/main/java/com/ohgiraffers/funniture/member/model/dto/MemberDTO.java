package com.ohgiraffers.funniture.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signupDate;

    private int isConsulting;

    private int hasImage;

    private String imageLink;

    private String imageId;

}