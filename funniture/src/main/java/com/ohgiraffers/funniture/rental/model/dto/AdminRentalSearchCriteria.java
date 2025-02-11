package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminRentalSearchCriteria {

    private String rentalState;
    private String storeName;
    private String categoryName;
    private LocalDateTime searchDate;
    private String rentalNo;
}
