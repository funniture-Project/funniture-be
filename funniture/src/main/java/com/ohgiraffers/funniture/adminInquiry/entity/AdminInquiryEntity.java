package com.ohgiraffers.funniture.adminInquiry.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_inquiryadmin")
public class AdminInquiryEntity {

    @Id
    private String id;

}
