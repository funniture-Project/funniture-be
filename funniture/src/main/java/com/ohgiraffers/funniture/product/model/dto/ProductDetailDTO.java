package com.ohgiraffers.funniture.product.model.dto;

import com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductDetailDTO {

    private String productNo;
    private String productName;
    private OwnerInfoDTO ownerInfo;

    private int totalStock;
    private Integer usedStock;

    private CategoryDTO category;

    // 정산 판매가
    private int regularPrice;

    // 상품 설명
    private String productContent;

    // 판매 상태
    private String productStatus;

    // 대표이미지 링크
    private String productImageLink;

    // 대표이미지 ID
    private String productImageId;

    // 대여 조건 리스트
    private List<RentalOptionInfoDTO> rentalOptionList;
}
