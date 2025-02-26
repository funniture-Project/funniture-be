package com.ohgiraffers.funniture.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohgiraffers.funniture.member.entity.MemberAndPointEntity;

import lombok.*;

import java.sql.Timestamp;
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

    public MemberAndPointDTO(Object[] obj) {
        this.memberId = obj.length > 0 ? (String) obj[0] : null;
        this.userName = obj.length > 1 ? (String) obj[1] : null;
        this.phoneNumber = obj.length > 2 ? (String) obj[2] : null;
        this.email = obj.length > 3 ? (String) obj[3] : null;
        this.signupDate = obj.length > 4 && obj[4] != null ? ((Timestamp) obj[4]).toLocalDateTime() : null; // ✅ Timestamp 변환
        this.memberRole = obj.length > 5 ? (String) obj[5] : null;
        this.pointDTO = new PointDTO(obj.length > 6 && obj[6] != null ? ((Number) obj[6]).intValue() : 0);
    }
}


