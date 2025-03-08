package com.ohgiraffers.funniture.adminInquiry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_inquiryadmin")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminInquiryEntity {

    @Id
    @Column(name = "inquiry_admin_no")
    private String inquiryAdminNo;

    @Column(name = "sender_no")
    private String senderNo;

    @Column(name = "receive_no")
    private String receiveNo;

    @Column(name = "contents")
    private String contents;

    @Column(name = "create_date_time", nullable = true, insertable = false)
    private LocalDateTime createDateTime;

}
