package com.ohgiraffers.funniture.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PagingButton {

    private int currentPage;
    private int startPage;
    private int endPage;

}
