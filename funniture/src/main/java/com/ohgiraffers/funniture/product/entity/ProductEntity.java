package com.ohgiraffers.funniture.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "tbl_product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
@DynamicInsert
@DynamicUpdate
public class ProductEntity {
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

    @Column(name = "category_code")
    private int categoryCode;

    // 정산 판매가
    @Column(name = "regular_price")
    private int regularPrice;

    // 상품 설명
    @Column(name = "product_content", columnDefinition = "MEDIUMTEXT")
    private String productContent;

    // 판매 상태
    @Column(name = "product_status", nullable = true)
    private String productStatus;

    // 대표 이미지 링크
    @Column(name = "product_image_link", nullable = true)
    private String productImageLink;

    // 대표 이미지 ID(삭제 시 필요)
    @Column(name = "product_image_id", nullable = true)
    private String productImageId;

    // ✅ 값이 존재하면 업데이트
    public void update(ProductEntity updatedProduct) {
        if (updatedProduct.productName != null) this.productName = updatedProduct.productName;
        if (updatedProduct.ownerNo != null) this.ownerNo = updatedProduct.ownerNo;
        if (updatedProduct.totalStock != 0) this.totalStock = updatedProduct.totalStock;
        if (updatedProduct.usedStock != 0) this.usedStock = updatedProduct.usedStock;
        if (updatedProduct.categoryCode != 0) this.categoryCode = updatedProduct.categoryCode;
        if (updatedProduct.regularPrice != 0) this.regularPrice = updatedProduct.regularPrice;
        if (updatedProduct.productContent != null) this.productContent = updatedProduct.productContent;
        if (updatedProduct.productStatus != null) this.productStatus = updatedProduct.productStatus;
        if (updatedProduct.productImageLink != null) this.productImageLink = updatedProduct.productImageLink;
        if (updatedProduct.productImageId != null) this.productImageId = updatedProduct.productImageId;
    }
}
