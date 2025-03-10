package com.ohgiraffers.funniture.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
// 관리자 페이지에서 제공자로 전환 요청한 회원의 모달에 들어갈 데이터들
public class AppOwnerListModalDTO {

    private String memberId;
    private String userName;
    private String phoneNumber;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime signupDate;

    private String memberRole;

    private OwnerInfoDTO ownerInfoDTO;
}
