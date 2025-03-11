package com.ohgiraffers.funniture.comment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentByMyPageDTO {

    private int commentNo;
    private String memberId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentWriteTime;

    private String commentContent;

    private int commentLevel;

    private Integer parentCommentNo;

    private String inquiryNo;
}
