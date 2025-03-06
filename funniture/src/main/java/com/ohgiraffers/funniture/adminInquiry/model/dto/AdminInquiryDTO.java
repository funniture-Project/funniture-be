package com.ohgiraffers.funniture.adminInquiry.model.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminInquiryDTO {

    private String inquiryAdminNo;
    private String senderNo;
    private String receiveNo;
    private String contents;
    private LocalDateTime createDateTime;
}
