package com.ohgiraffers.funniture.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_ownerinfo", uniqueConstraints = {
        @UniqueConstraint(columnNames = "member_id")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class OwnerInfoEntity {

    @Column(name = "member_id", unique = true) // 유니크 제약 조건 추가
    private String memberId;

    @Id
    @Column(name = "store_no")
    private String storeNo;

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

    // 25-02-26 컬럼 추가돼서 아래부터 추가
    @Column(name = "is_rejected")
    private int isRejected;

    @Column (name = "store_image")
    private String storeImage;

    @Column(name = "store_phone")
    private String storePhone;

    // 관리자 페이지에서 제공자 정보 필요해서 조인컬럼
    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", insertable = false, updatable = false)
    private MemberEntity member;

}
