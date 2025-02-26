package com.ohgiraffers.funniture.product.model.dto;


import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
public class ChangeStatusDTO {

    List<String> productList;
    String changeStatus;
}
