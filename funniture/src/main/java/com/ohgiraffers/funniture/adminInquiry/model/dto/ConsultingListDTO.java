package com.ohgiraffers.funniture.adminInquiry.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsultingListDTO {

    private String memberId;
    private String userName;
    private String email;
    private Boolean isConsulting;
    private String memberRole;
    private String imageLink;

}
