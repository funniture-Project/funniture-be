package com.ohgiraffers.funniture.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

// 자신이 필요한걸로 변경해서 하세요!
@Entity
@Table(name = "tbl_product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDetailEntity {

    @Id
    @Column(name = "product_no")
    private String productNo;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "owner_no")
    private String ownerNo;

    @Column(name = "total_stock")
    private int totalStock;

    @Column(name = "used_stock")
    private int usedStock;

    @ManyToOne
    @JoinColumn(name = "category_code")
    private CategoryEntity category;

    // 정산 판매가
    @Column(name = "regular_price")
    private int regularPrice;

    // 상품 설명
    @Column(name = "product_content")
    private String productContent;

    // 판매 상태
    @Column(name = "product_status")
    private String productStatus;

    // 대표 이미지 링크
    @Column(name = "product_image_link")
    private String productImageLink;

    // 대표 이미지 ID(삭제 시 필요)
    @Column(name = "product_image_id")
    private String productImageId;

    // 상품 대여조건 리스트
    @OneToMany
    @JoinColumn(name = "product_no")
    private List<RentalOptionInfoEntity> rentalOptionList;
}
