package com.ohgiraffers.funniture.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "tbl_category")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CategoryEntity {

    @Id
    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "ref_category_code")
    private Integer refCategoryCode;

    @Column(name = "category_image")
    private String categoryImage;
}
