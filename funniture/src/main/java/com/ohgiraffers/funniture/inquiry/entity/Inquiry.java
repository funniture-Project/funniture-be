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

//    @Column
//    private String memberId;

// 문의를 작성한 회원 (Member와 ManyToOne 관계)
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @Column
    private String inquiryContent;

    @Column
    private int showStatus;

    @Column
    private int qnaType;

//    private String ownerNo;

//    @Column
//    private String productNo;

    @ManyToOne
    @JoinColumn(name = "product_no", referencedColumnName = "product_no")  // Product와 연결
    private Product product;

    @Column
    private LocalDateTime qnaWriteTime;

}
