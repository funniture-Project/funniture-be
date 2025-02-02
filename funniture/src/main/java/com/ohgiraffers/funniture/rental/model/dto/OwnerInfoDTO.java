package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OwnerInfoDTO {

    private String storeNo;         // 사업자 등록번호(PK)

    private String ownerId;        // 제공자 회원번호(FK)

    private String storeName;       // 사업장 이름

    private String storeAddress;    // 사업장 주소

    private String account;         // 계좌 정보

    private String bank;            // 은행

    private String attechmentLink;  // 첨부파일 주소


}
