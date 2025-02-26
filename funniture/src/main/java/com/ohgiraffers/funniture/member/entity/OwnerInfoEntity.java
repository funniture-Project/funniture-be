package com.ohgiraffers.funniture.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_ownerinfo", uniqueConstraints = {
        @UniqueConstraint(columnNames = "member_id")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OwnerInfoEntity {

    @Id
    @Column(name = "store_no")
    private String storeNo;

    @Column(name = "member_id", unique = true) // 유니크 제약 조건 추가
    private String memberId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_address")
    private String storeAddress;

    @Column(name = "account")
    private String account;

    @Column(name = "bank")
    private String bank;

    @Column(name = "attechment_link")
    private String attechmentLink;
}
