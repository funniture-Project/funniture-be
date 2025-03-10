package com.ohgiraffers.funniture.member.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OwnerInfoDTO {

    private String storeNo;
    private String memberId;
    private String storeName;
    private String storeAddress;
    private String account;
    private String bank;
    private String attechmentLink;

    // 컬럼 추가 25-02-27
    private int isRejected;
    private String storeImage;
    private String storePhone;

}
