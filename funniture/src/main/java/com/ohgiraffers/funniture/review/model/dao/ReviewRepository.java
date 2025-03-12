package com.ohgiraffers.funniture.review.model.dao;

import com.ohgiraffers.funniture.review.entity.ReviewEntity;
import com.ohgiraffers.funniture.review.model.dto.ReviewAvgScoreDTO;
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

    // 작성한 리뷰 조회
    @Query(value = "SELECT r.review_no, r.review_write_time, r.review_content, r.member_id, r.product_no, r.score, " +
            "p.product_name, p.product_image_link, rt.rental_state " +
            "FROM tbl_review r " +
            "LEFT JOIN tbl_product p ON r.product_no = p.product_no " +
            "LEFT JOIN tbl_rental rt ON r.product_no = rt.product_no AND r.member_id = rt.member_id " +
            "WHERE r.member_id = :memberId " +
            "AND rt.rental_state IN ('배송완료', '반납요청', '수거중', '반납완료') " +
            "AND r.review_content IS NOT NULL " +
            "ORDER BY r.review_write_time DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Object[]> findWrittenReviews(@Param("memberId") String memberId, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM tbl_review r " +
            "LEFT JOIN tbl_rental rt ON r.product_no = rt.product_no AND r.member_id = rt.member_id " +
            "WHERE r.member_id = :memberId " +
            "AND rt.rental_state IN ('배송완료', '반납요청', '수거중', '반납완료') " +
            "AND r.review_content IS NOT NULL", nativeQuery = true)
    int countWrittenReviews(@Param("memberId") String memberId);



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

    // 메인 페이지 리뷰 달릴 애
    @Query(value = "SELECT r.review_no, r.review_write_time, r.review_content, r.member_id, r.product_no, r.score, p.product_name, m.user_name " +
            "FROM tbl_review r " +
            "JOIN tbl_product p ON r.product_no = p.product_no " +
            "JOIN tbl_member m ON r.member_id = m.member_id " +
            "ORDER BY r.review_write_time DESC", nativeQuery = true)
    List<Object[]> findAllReviewByMain();

    // 제공자 메인 페이지 평점 조회
    @Query(value = "SELECT p.product_no, p.product_name, AVG(r.score) AS score " +
            "FROM tbl_review r " +
            "JOIN tbl_product p ON r.product_no = p.product_no " +
            "WHERE p.owner_no = :ownerNo " +
            "GROUP BY p.product_no, p.product_name " +
            "ORDER BY AVG(r.score) DESC", nativeQuery = true)
    List<Object[]> findReviewAverageByOwnerNative(@Param("ownerNo") String ownerNo);

}

