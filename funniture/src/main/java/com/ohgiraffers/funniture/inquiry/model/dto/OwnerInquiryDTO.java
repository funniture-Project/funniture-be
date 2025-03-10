package com.ohgiraffers.funniture.inquiry.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OwnerInquiryDTO {

    private String inquiryNo;
    private String memberId;

    private String inquiryContent;
    private int showStatus;
    private int qnaType;
    private String productNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime qnaWriteTime;

//    private MemberDTO memberDTO;
//    private ProductDTO productDTO;

    // InquiryEntity에 해당되지 않는 userName(member)와 productName(product)를
    // 연관관계 형성하지 않고 삽입
    private String userName;
    private String phoneNumber;
    private String productName;

    private String productImageLink;

//    private int commentNo;

    private String answerStatus;

}