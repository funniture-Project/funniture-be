package com.ohgiraffers.funniture.member.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountCombinedKey implements Serializable {

    private LocalDate connectDate;
    private String connectAuth;
}
