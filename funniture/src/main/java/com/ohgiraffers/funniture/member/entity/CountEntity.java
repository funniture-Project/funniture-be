package com.ohgiraffers.funniture.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_connecttotal")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@IdClass(CountCombinedKey.class)
public class CountEntity {

    @Id
    @Column(name = "connect_date")
    private LocalDate connectDate;

    @Id
    @Column(name = "connect_auth")
    private String connectAuth;

    @Column(name = "connect_count")
    private Integer connectCount;

}
