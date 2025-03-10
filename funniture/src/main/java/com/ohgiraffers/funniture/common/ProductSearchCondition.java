package com.ohgiraffers.funniture.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductSearchCondition {

    private List<Integer> categoryCode;

    private List<String> ownerNo;

    private String productStatus;

    private String searchText;

    private Integer pageNum;

}
