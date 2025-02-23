package com.ohgiraffers.funniture.point.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "pointHistory")
@Table(name = "tbl_pointhistory")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PointHistoryEntity {

    @Id
    @Column(name = "point_history_id")
    private int pointHistoryId;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "used_point")
    private int usedPoint;
}
