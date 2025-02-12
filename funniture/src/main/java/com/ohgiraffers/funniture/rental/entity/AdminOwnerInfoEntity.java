package com.ohgiraffers.funniture.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_ownerinfo")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminOwnerInfoEntity {

    @Column(name = "member_id")
    private String memberId;

    @Id
    @Column(name = "store_no")
    private String storeNo;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_adress")
    private String storeAdress;

    @Column(name = "account")
    private String account;

    @Column(name = "bank")
    private String bank;

    @Column(name = "attechment_link")
    private String attechmentLink;

}
