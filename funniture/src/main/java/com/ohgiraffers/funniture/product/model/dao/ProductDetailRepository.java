package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.ProductDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, String> {
    // category 에서의 category_code 를 비교해야하기 때문에 findByCategory_CategoryCode
//    @Query(value = "SELECT p FROM tbl_product p WHERE p.category.categoryCode IN :categoryCode", nativeQuery = true)
//    List<ProductDetailEntity> findByCategory_CategoryCodeIn(@Param("categoryCode") List<Integer> categoryCode);

}
