package com.ohgiraffers.funniture.notice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_notice")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class NoticeEntity {

    @Id
    @Column(name = "notice_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeNo;

    @Column(name = "notice_title")
    private String noticeTitle;

    @Column(name = "view_roll")
    private String viewRoll;

    @Column(name = "notice_content")
    private String noticeContent;

    @Column(name = "write_time", nullable = true, insertable = false)
    private LocalDateTime writeTime;

}
