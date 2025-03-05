package com.ohgiraffers.funniture.point.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private int usedPoint;     // 사용 포인트

    private int addPoint;      // 충전 포인트

    private int currentPoint;   // 현재 보유 포인트

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime pointDateTime;    // 포인트 시간

}
