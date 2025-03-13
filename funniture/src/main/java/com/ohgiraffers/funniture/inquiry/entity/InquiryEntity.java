package com.ohgiraffers.funniture.inquiry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 자신이 필요한걸로 변경해서 하세요!
@Entity
@Table(name = "tbl_inquiry")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Builder(toBuilder = true)
public class InquiryEntity {

    @Id
    @Column (name = "inquiry_no")
    private String inquiryNo;

    @Column (name = "member_id")
    private String memberId;

    @Column (name = "inquiry_content")
    private String inquiryContent;

    @Column (name = "show_status")
    private int showStatus;

    @Column (name = "qna_type")
    private int qnaType;

    @Column (name = "product_no")
    private String productNo;

    @Column (name = "qna_write_time")
    private LocalDateTime qnaWriteTime;

    // InquiryEntity에 해당되지 않는 userName(member)와 productName(product)를
    // 연관관계 형성하지 않고 삽입
    @Transient
    private String userName;

    @Transient
    private String productName;

    @Transient
    private String phoneNumber;

    @Transient
    private int commentNo;

    @Transient
    private String answerStatus;
}
