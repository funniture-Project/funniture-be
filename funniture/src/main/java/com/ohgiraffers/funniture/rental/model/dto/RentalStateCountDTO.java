package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RentalStateCountDTO {
    private String rentalState;
    private long count;
}
