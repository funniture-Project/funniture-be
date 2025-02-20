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
    private String storeAdress;
    private String account;
    private String bank;
    private String attechmentLink;

}
