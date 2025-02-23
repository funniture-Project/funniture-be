package com.ohgiraffers.funniture.point.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity(name = "point")
@Table(name = "tbl_point")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointEntity {

    @Id
    @Column(name = "member_id")
    private String memberId;    // 회원번호

    @Column(name = "initial_point")
    private int initialPoint;      // 보유 포인트

    @Column(name = "add_point")
    private int addPoint;   // 충전 포인트

}
