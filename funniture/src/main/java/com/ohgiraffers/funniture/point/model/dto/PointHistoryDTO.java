package com.ohgiraffers.funniture.point.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PointHistoryDTO {

    private int pointHistoryId;

    private String memberId;

    private int usedPoint;


}
