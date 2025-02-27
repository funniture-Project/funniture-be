package com.ohgiraffers.funniture.member.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AppOwnerInfoDTO {

    private String memberId;
    private String storeNo;
    private String storeName;
    private String storeAddress;
    private String account;
    private String bank;
    private String attechmentLink;
    private String isRejected;
    private String storeImage;
    private String storePhone;
}
