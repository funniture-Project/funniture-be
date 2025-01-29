package com.ohgiraffers.funniture.inquiry.model.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InquiryDTO {

    private String inquiryNo;
    private String memberId;
    private String inquiryContent;
    private int showStatus;
    private int qnaType;
    private String productNo;
    private LocalDateTime qnaWriteTime;
}
