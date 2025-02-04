package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.ProductDetailEntity;
import com.ohgiraffers.funniture.product.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @Query(value = "SELECT MAX(product_no) FROM tbl_product", nativeQuery = true)
    String findMaxNo();

    @Query(value = "SELECT * FROM tbl_product p WHERE p.category_code IN :categoryCode", nativeQuery = true)
    List<ProductEntity> findByCategoryCodeIn(@Param("categoryCode") List<Integer> categoryCode);
}
