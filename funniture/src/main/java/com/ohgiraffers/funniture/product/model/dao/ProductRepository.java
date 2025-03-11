package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.ProductEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @Query(value = "SELECT MAX(product_no) FROM tbl_product", nativeQuery = true)
    String findMaxNo();

    @Query(value = "SELECT * FROM tbl_product p WHERE p.category_code IN :categoryCode", nativeQuery = true)
    List<ProductEntity> findByCategoryCodeIn(@Param("categoryCode") List<Integer> categoryCode);

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
            COALESCE(GROUP_CONCAT(ri.rental_price ORDER BY ri.rental_price ASC), '') AS price_list
        FROM tbl_product p
        LEFT JOIN tbl_rentaloptioninfo ri ON p.product_no = ri.product_no
        GROUP BY p.product_no
        """, nativeQuery = true)
    List<Object[]> findAllProductsWithPriceList();

    @Query(value = """
        SELECT DISTINCT
            oi.store_name,
            p.owner_no
        FROM tbl_product p
        LEFT JOIN tbl_ownerinfo oi ON p.owner_no = oi.member_id
        WHERE p.category_code in :categoryCode
        """, nativeQuery = true)
    List<Object[]> getOwnerByCategory(List<Integer> categoryCode);

    @Query(value = """
        SELECT DISTINCT
            oi.store_name,
            p.owner_no
        FROM tbl_product p
        LEFT JOIN tbl_ownerinfo oi ON p.owner_no = oi.member_id
        LEFT JOIN tbl_category c ON p.category_code = c.category_code
        WHERE c.ref_category_code in :categoryCode
        """, nativeQuery = true)
    List<Object[]> getOwnerByRefCategory(List<Integer> categoryCode);

    // 렌탈 등록 - 사용 재고 증가
    // @Query, @Modifying 함께 사용될 때,
    // 데이터베이스 레벨에서 바로 업데이트가 이루어지므로 효율적이고 안전하게 동시성 문제를 처리 가능
    @Modifying
    @Query("UPDATE ProductEntity p SET p.usedStock = p.usedStock + :rentalNumber WHERE p.productNo = :productNo")
    void incrementUsedStock(String productNo, int rentalNumber);

    @Query(value = """
        SELECT c.category_name, count(p.product_no)
        FROM 
            tbl_product p
        JOIN tbl_category c ON p.category_code = c.category_code
        WHERE p.product_status = "판매중" OR p.product_status = "품절"
        GROUP BY p.category_code
        """, nativeQuery = true)
    List<Object[]> countByCategory();
}
