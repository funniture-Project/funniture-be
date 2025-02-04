package com.ohgiraffers.funniture.inquiry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 자신이 필요한걸로 변경해서 하세요!
@Entity (name = "inquiry")
@Table(name = "tbl_inquiry")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Inquiry {

    @Id
    @Column
    private String inquiryNo;

    @Column
    private String memberId;

    @Column
    private String inquiryContent;

    @Column
    private int showStatus;

    @Column
    private int qnaType;

    @Column
    private String productNo;

    @Column
    private LocalDateTime qnaWriteTime;

}
