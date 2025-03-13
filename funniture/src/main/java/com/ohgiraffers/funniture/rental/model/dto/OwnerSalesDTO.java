package com.ohgiraffers.funniture.rental.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OwnerSalesDTO {

    private String rentalNo;

    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime orderDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime rental_start_date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime rental_end_date;

    private int rentalNumber;

    private String productNo;

    private String productName;

    private int rentalPrice;
}
