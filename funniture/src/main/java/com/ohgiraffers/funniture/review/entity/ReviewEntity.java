package com.ohgiraffers.funniture.review.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_review")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Builder(toBuilder = true)
public class ReviewEntity {

    @Id
    @Column (name = "review_no")
    private String reviewNo;

    @Column (name = "review_write_time")
    private LocalDateTime reviewWriteTime;

    @Column (name = "review_content")
    private String reviewContent;

    @Column (name = "member_id")
    private String memberId;

    @Column (name = "product_no")
    private String productNo;

    @Column (name = "score")
    private float score;

    @Transient
    private String productName;

    @Transient
    private String productImageLink;

    @Transient
    private String userName;

    @Transient
    private int rentalTerm;

    @Transient
    private String rentalState;

    @Transient
    private String rentalNo;

    @Transient
    private String orderDate;

    @Transient
    private int rentalPrice;
}
