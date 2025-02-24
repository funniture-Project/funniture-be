package com.ohgiraffers.funniture.point.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "point")
@Table(name = "tbl_point")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointEntity {

    @Id
    @Column(name = "point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pointId;    // 포인트 id

    @Column(name = "member_id")
    private String memberId;    // 회원번호

    @Column(name = "used_point")
    private int used_point;     // 사용 포인트

    @Column(name = "add_point")
    private int addPoint;      // 충전 포인트

    @Column(name = "current_point")
    private int currentPoint;   // 현재 보유 포인트

    @Column(name = "point_date_time", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime pointDateTime;    // 포인트 시간


}
