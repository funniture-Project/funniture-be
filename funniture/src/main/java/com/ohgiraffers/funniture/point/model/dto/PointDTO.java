package com.ohgiraffers.funniture.point.model.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PointDTO {

    private int pointId;    // 포인트 id

    private String memberId;    // 회원번호

    private int used_point;     // 사용 포인트

    private int addPoint;      // 충전 포인트

    private int currentPoint;   // 현재 보유 포인트

    private LocalDateTime pointDateTime;    // 포인트 시간

}
