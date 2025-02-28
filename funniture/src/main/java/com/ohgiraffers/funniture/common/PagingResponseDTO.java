package com.ohgiraffers.funniture.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PagingResponseDTO {

    // (응답 데이터 구조)

    private Object data;   // 실제 데이터 목록
    private PageDTO pageInfo;  // 페이징 정보

}
