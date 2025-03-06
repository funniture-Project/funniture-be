package com.ohgiraffers.funniture.adminInquiry.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDateTime;
}
