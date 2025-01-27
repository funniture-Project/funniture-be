package com.ohgiraffers.funniture.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
// 사용자에게 보여 줄 메시지 클래스
public class Message {
    // DTO 처럼 사용
    private int httpStatusCode;
    private String message;
}
