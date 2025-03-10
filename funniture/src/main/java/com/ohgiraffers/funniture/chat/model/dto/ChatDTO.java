package com.ohgiraffers.funniture.chat.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatDTO {

    private String chatQaNo;
    private int chatQaLevel;
    private String chatQaQuContent;
    private String chatQaAnContent;
    private Boolean nextQuAbsence;
    private Boolean adminConnectAbsence;
    private Integer refQuNo;

}
