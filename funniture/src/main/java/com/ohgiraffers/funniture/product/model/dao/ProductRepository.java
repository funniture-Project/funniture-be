package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// 25-01-27 16:41분 / 이쪽도 주석해야 run 됨
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
