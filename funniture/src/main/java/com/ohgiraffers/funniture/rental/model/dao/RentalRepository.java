package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Product, String> {
}
