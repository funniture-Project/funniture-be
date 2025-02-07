package com.ohgiraffers.funniture.rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminCategoryEntity {

    @Id
    @Column(name = "category_code")
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "ref_category_code", referencedColumnName = "category_code")
    private AdminCategoryEntity refCategoryCode;
}
