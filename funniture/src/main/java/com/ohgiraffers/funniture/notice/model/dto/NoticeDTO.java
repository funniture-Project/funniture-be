package com.ohgiraffers.funniture.notice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeDTO {

    private Integer noticeNo;
    private String noticeTitle;
    private String viewRoll;
    private String noticeContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime writeTime;
}
