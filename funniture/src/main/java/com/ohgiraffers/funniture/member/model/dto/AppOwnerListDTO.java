package com.ohgiraffers.funniture.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AppOwnerListDTO {
    // 관리자 페이지에서 제공자 전환 신청 한 목록 리스트 매핑
    private String memberId;
    private String email;
    private String userName;
    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime signupDate;

    private String memberRole;

    private int isRejected;  // ✅ 직접 매핑

    public AppOwnerListDTO(Object[] obj) {
        this.memberId = obj.length > 0 ? (String) obj[0] : null;
        this.userName = obj.length > 1 ? (String) obj[1] : null;
        this.phoneNumber = obj.length > 2 ? (String) obj[2] : null;
        this.email = obj.length > 3 ? (String) obj[3] : null;
        this.signupDate = obj.length > 4 && obj[4] != null ? ((Timestamp) obj[4]).toLocalDateTime() : null; // ✅ Timestamp 변환
        this.memberRole = obj.length > 5 ? (String) obj[5] : null;
        // ✅ isRejected 형변환 수정
        if (obj.length > 6 && obj[6] != null) {
            if (obj[6] instanceof Number) {
                this.isRejected = ((Number) obj[6]).intValue();
            } else if (obj[6] instanceof String) {
                try {
                    this.isRejected = Integer.parseInt((String) obj[6]);
                } catch (NumberFormatException e) {
                    this.isRejected = 0; // 예외 발생 시 기본값
                }
            } else {
                this.isRejected = 0; // 예외 처리
            }
        } else {
            this.isRejected = 0;
        }
    }
}
