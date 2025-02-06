package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.ProductWithPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductWithPriceRepository extends JpaRepository<ProductWithPriceEntity, String> {

    @Query(value = """
        SELECT 
            p.product_no, 
            p.product_name, 
            p.owner_no, 
            p.total_stock, 
            p.used_stock, 
            p.category_code, 
            p.regular_price, 
            p.product_content, 
            p.product_status, 
            p.product_image_link, 
            p.product_image_id, 
            COALESCE(GROUP_CONCAT(ri.rental_price ORDER BY ri.rental_price ASC SEPARATOR ','), '') AS price_list
        FROM tbl_product p
        LEFT JOIN tbl_rentaloptioninfo ri ON p.product_no = ri.product_no
        GROUP BY p.product_no
        """, nativeQuery = true)
    List<ProductWithPriceEntity> findAllProductsList();

    @Query(value = """
        SELECT 
            p.product_no, 
            p.product_name, 
            p.owner_no, 
            p.total_stock, 
            p.used_stock, 
            p.category_code, 
            p.regular_price, 
            p.product_content, 
            p.product_status, 
            p.product_image_link, 
            p.product_image_id, 
            COALESCE(GROUP_CONCAT(ri.rental_price ORDER BY ri.rental_price ASC SEPARATOR ','), '') AS price_list
        FROM tbl_product p
        LEFT JOIN tbl_rentaloptioninfo ri ON p.product_no = ri.product_no
        WHERE (p.category_code IN (:categoryCode))
        GROUP BY p.product_no
        """, nativeQuery = true)
    List<ProductWithPriceEntity> findAllProductsListByCategoryIn(List<Integer> categoryCode);
}
