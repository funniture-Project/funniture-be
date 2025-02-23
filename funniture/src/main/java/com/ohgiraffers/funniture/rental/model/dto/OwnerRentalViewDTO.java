package com.ohgiraffers.funniture.rental.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OwnerRentalViewDTO {

    private String rentalNo;

    private String deliverCom;

    private String deliveryNo;

    private int rentalNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime rentalStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime rentalEndDate;

    private String rentalState;

    private String productName;

    private int rentalTerm;

    private int asNumber;

}
