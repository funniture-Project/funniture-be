package com.ohgiraffers.funniture.comment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Builder(toBuilder = true)
public class CommentEntity {

    @Id
    @Column (name = "comment_no")
    private int commentNo;

    @Column (name = "member_id")
    private String memberId;

    @Column (name = "comment_write_time")
    private LocalDateTime commentWriteTime;

    @Column (name = "comment_content")
    private String commentContent;

    @Column (name = "comment_level")
    private Integer commentLevel;

    @Column (name = "parent_comment_no")
    private int parentCommentNo;

    @Column (name = "inquiry_no")
    private String inquiryNo;

}
