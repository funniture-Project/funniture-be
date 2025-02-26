package com.ohgiraffers.funniture.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohgiraffers.funniture.member.entity.MemberAndPointEntity;
import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

// 관리자 페이지에서 제공자 정보 가져올 때 여기 DTO 매핑해서 가져옴
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OwnerInfoAndMemberDTO {

    private String memberId;
    private String storeNo;
    private String storeName;
    private String storePhone;
    private String userName;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime signupDate;
    private String memberRole;

    public OwnerInfoAndMemberDTO(Object[] obj) {
        this.memberId = (String) obj[0];
        this.storeNo = (String) obj[1];
        this.storeName = (String) obj[2];
        this.storePhone = (String) obj[3];
        this.userName = (String) obj[4];
        this.email = (String) obj[5];
        this.memberRole = (String) obj[7];

        // obj[6]가 Timestamp일 경우 LocalDateTime으로 변환
        if (obj[6] instanceof Timestamp) {
            this.signupDate = ((Timestamp) obj[6]).toLocalDateTime();
        } else {
            this.signupDate = null; // obj[6]가 null이거나 다른 타입일 경우
        }
    }
}
