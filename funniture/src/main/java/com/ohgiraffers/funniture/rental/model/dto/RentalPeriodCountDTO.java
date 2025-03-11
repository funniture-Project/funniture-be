package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RentalPeriodCountDTO {

    private String period; // 기간 이름 (예: "세달전", "한달전", "일주전")

    private Long count;    // 해당 기간의 예약 건수

}
