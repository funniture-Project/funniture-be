package com.ohgiraffers.funniture.point.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PointDTO {

    private String memberId;    // 회원번호

    private int usedPoint;      // 사용 포인트

    private int remainPoint;    // 잔여 포인트

    private LocalDateTime usedDateTime; // 사용 날짜

    private int addPoint;   // 충전 포인트
}
