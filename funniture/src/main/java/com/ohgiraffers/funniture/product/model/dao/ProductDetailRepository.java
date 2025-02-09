package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.ProductDetailEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, String> {
    List<ProductDetailEntity> findAllByOwnerNo(String ownerNo);
}
