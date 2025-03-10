package com.ohgiraffers.funniture.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConnectCountDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate connectDate;
    private String connectAuth;
    private Integer connectCount;
}
