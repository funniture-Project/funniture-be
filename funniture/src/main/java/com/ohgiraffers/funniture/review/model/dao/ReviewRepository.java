package com.ohgiraffers.funniture.review.model.dao;

import com.ohgiraffers.funniture.review.entity.ReviewEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity , String> {

    @Query(value = "SELECT MAX(REVIEW_NO) FROM TBL_REVIEW",
            nativeQuery = true)
    String maxReview();

    @Query(value = "SELECT r.review_no, r.review_write_time, r.review_content, r.member_id, r.product_no, r.score, " +
            "p.product_name, p.product_image_link " +
            "FROM tbl_review r " +
            "LEFT JOIN tbl_product p ON r.product_no = p.product_no " +
            "WHERE r.member_id = :memberId " +
            "ORDER BY r.review_write_time DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Object[]> findAllReviewUserPage(@Param("memberId") String memberId, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM tbl_review WHERE member_id = :memberId", nativeQuery = true)
    int countAllReviewUserPage(@Param("memberId") String memberId);


    @Query(value = "SELECT " +
            "r.review_no, " +
            "r.review_write_time, " +
            "r.review_content, " +
            "r.member_id, " +
            "r.product_no, " +
            "r.score, " +
            "p.product_name, " +
            "p.product_image_link, " +
            "m.user_name, " +
            "ro.rental_term " +
            "FROM tbl_review r " +
            "JOIN tbl_product p ON r.product_no = p.product_no " +
            "JOIN tbl_member m ON r.member_id = m.member_id " +
            "JOIN tbl_rentaloptioninfo ro ON r.product_no = ro.product_no " +
            "WHERE r.product_no = ?1", nativeQuery = true)
    List<Object[]> findDetailedReviewByProductNo(String productNo);

    @Query(value = "SELECT DISTINCT " +
            "r.review_no, " +
            "r.review_write_time, " +
            "r.review_content, " +
            "r.member_id, " +
            "r.product_no, " +
            "r.score, " +
            "p.product_name, " +
            "p.product_image_link, " +
            "m.user_name, " +
            "MIN(ro.rental_term) AS rental_term " +
            "FROM tbl_review r " +
            "JOIN tbl_product p ON r.product_no = p.product_no " +
            "JOIN tbl_member m ON r.member_id = m.member_id " +
            "JOIN tbl_rentaloptioninfo ro ON r.product_no = ro.product_no " +
            "WHERE p.owner_no = ?1 " +
            "GROUP BY r.review_no, r.review_write_time, r.review_content, r.member_id, r.product_no, r.score, p.product_name, p.product_image_link, m.user_name " +
            "ORDER BY r.review_write_time DESC " +
            "LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Object[]> findReviewsOfProductsByOwner(String ownerNo, int limit, int offset);


    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_review r " +
            "JOIN tbl_product p ON r.product_no = p.product_no " +
            "JOIN tbl_member m ON r.member_id = m.member_id " +
            "JOIN tbl_rentaloptioninfo ro ON r.product_no = ro.product_no " +
            "WHERE p.owner_no = ?1", nativeQuery = true)
    int countReviewsOfProductsByOwner(String ownerNo);
}

