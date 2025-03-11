package com.ohgiraffers.funniture.inquiry.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberInquiryDTO {

    private String inquiryNo;
    private String memberId;

    private String inquiryContent;
    private int showStatus;
    private int qnaType;
    private String productNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime qnaWriteTime;

    private String userName;
    private String phoneNumber;
    private String productName;

    private String productImageLink;

    private String answerStatus;
}
