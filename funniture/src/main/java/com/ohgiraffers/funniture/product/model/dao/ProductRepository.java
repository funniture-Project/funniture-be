package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
