package com.ohgiraffers.funniture.inquiry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_inquiry")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Builder(toBuilder = true)
public class InquiryRegistEntity {

    @Id
    @Column(name = "inquiry_no")
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
}
