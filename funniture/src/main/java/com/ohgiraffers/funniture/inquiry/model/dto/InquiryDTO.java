package com.ohgiraffers.funniture.inquiry.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohgiraffers.funniture.product.model.dto.ProductDTO;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InquiryDTO {

    private String inquiryNo;

    private String memberId;

    private String inquiryContent;
//    private int showStatus;
//    private int qnaType;
    private String productNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime qnaWriteTime;

//    private MemberDTO memberDTO;
//    private ProductDTO productDTO;

    // InquiryEntity에 해당되지 않는 userName(member)와 productName(product)를
    // 연관관계 형성하지 않고 삽입
    private String userName;
    private String productName;

    // JPQL의 쿼리문과 순서 맞아야 함.
    public InquiryDTO(LocalDateTime qnaWriteTime, String userName, String memberId, String inquiryNo, String productNo, String productName, String inquiryContent) {
        this.qnaWriteTime = qnaWriteTime;
        this.userName = userName;
        this.memberId = memberId;
        this.inquiryNo = inquiryNo;
        this.productNo = productNo;
        this.productName = productName;
        this.inquiryContent = inquiryContent;
    }

}
