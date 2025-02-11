package com.ohgiraffers.funniture.rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminProductEntity {
    @Id
    @Column(name = "product_no")
    private String productNo;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "total_stock")
    private int totalStock;

    @Column(name = "used_stock")
    private int usedStock;

    // 정산 판매가
    @Column(name = "regular_price")
    private int regularPrice;

    // 상품 설명
    @Column(name = "product_content")
    private String productContent;

    // 판매 상태
    @Column(name = "product_status", insertable = false)
    private String productStatus;

    // 대표 이미지 링크
    @Column(name = "product_image_link", insertable = false)
    private String productImageLink;

    // 대표 이미지 ID(삭제 시 필요)
    @Column(name = "product_image_id", insertable = false)
    private String productImageId;

    @ManyToOne
    @JoinColumn(name = "category_code")
    private AdminCategoryEntity adminCategory;

    @ManyToOne
    @JoinColumn(name = "owner_no", referencedColumnName = "member_id")
    private AdminOwnerInfoEntity adminOwnerInfo;
}
