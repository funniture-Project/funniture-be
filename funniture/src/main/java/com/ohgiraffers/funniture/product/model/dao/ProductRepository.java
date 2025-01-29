package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 25-01-27 16:41분 / 이쪽도 주석해야 run 됨
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    // category 에서의 category_code 를 비교해야하기 때문에 findByCategory_CategoryCode
    List<ProductEntity> findByCategory_CategoryCode(int categoryCode);

}
