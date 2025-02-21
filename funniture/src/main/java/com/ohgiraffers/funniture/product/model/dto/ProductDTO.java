package com.ohgiraffers.funniture.product.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

    private String productNo;

    @NotNull(message = "상품명은 필수입니다.")
    @Size(min = 1, max = 30, message = "상품명은 1자 이상 30자 이내여야 합니다.")
    private String productName;

    @NotNull(message = "제공자 번호는 필수입니다.")
    private String ownerNo;

    @NotNull(message = "총 재고 수는 필수입니다.")
    @Min(value = 1, message = "전체 재고는 0보다 커야 합니다.")
    private int totalStock;

    private Integer usedStock;

    @NotNull(message = "카테고리는 필수입니다.")
    private int categoryCode;

    // 정산 판매가
    @NotNull(message = "정산 판매가는 필수입니다.")
    @Min(value = 1, message = "정산 판매가는 0 보다 커야 합니다.")
    private int regularPrice;

    // 상품 설명
    private String productContent;

    // 판매 상태
    private String productStatus;

    // 대표이미지 링크
    private String productImageLink;

    // 대표이미지 ID
    private String productImageId;
}
