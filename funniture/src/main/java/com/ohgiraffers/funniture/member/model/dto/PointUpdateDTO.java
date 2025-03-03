package com.ohgiraffers.funniture.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointUpdateDTO {
    private String memberId;
    private int newPoint;
}
