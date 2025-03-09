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

//    @Query(value = "SELECT r.review_no, r.review_write_time, r.review_content, r.member_id, r.product_no, r.score, " +
//            "p.product_name, p.product_image_link, rt.rental_state " + // rental_state를 tbl_rental에서 가져옴
//            "FROM tbl_review r " +
//            "LEFT JOIN tbl_product p ON r.product_no = p.product_no " +
//            "LEFT JOIN tbl_rental rt ON r.product_no = rt.product_no AND r.member_id = rt.member_id " + // rental 테이블과 조인
//            "WHERE r.member_id = :memberId AND rt.rental_state = '예약완료' " + // rental_state 조건 추가
//            "ORDER BY r.review_write_time DESC " +
//            "LIMIT :limit OFFSET :offset", nativeQuery = true)
//    List<Object[]> findAllReviewUserPage(@Param("memberId") String memberId, @Param("limit") int limit, @Param("offset") int offset);
//
//    @Query(value = "SELECT COUNT(*) FROM tbl_review r " +
//            "LEFT JOIN tbl_rental rt ON r.product_no = rt.product_no AND r.member_id = rt.member_id " +
//            "WHERE r.member_id = :memberId AND rt.rental_state = '예약완료'", nativeQuery = true)
//    int countAllReviewUserPage(@Param("memberId") String memberId);

    // 작성 가능한 리뷰 조회
//    @Query(value = "SELECT rt.rental_no, rt.order_date, p.product_no, p.product_name, p.product_image_link, " +
//            "roi.rental_term, roi.rental_price " +
//            "FROM tbl_rental rt " +
//            "LEFT JOIN tbl_product p ON rt.product_no = p.product_no " +
//            "LEFT JOIN tbl_rentaloptioninfo roi ON rt.rental_no = roi.rental_no " +
//            "LEFT JOIN tbl_review r ON rt.product_no = r.product_no AND rt.member_id = r.member_id " + // 수정된 조인 조건
//            "WHERE rt.member_id = :memberId AND rt.rental_state = '예약완료' AND r.review_no IS NULL " + // 조건: 예약완료 + 리뷰 없음
//            "ORDER BY rt.order_date DESC " +
//            "LIMIT :limit OFFSET :offset", nativeQuery = true)
//    List<Object[]> findWritableReviews(@Param("memberId") String memberId, @Param("limit") int limit, @Param("offset") int offset);
//
//
//    @Query(value = "SELECT COUNT(*) FROM tbl_rental rt " +
//            "LEFT JOIN tbl_review r ON rt.rental_no = r.rental_no " +
//            "WHERE rt.member_id = :memberId AND rt.rental_state = '예약완료' AND r.review_no IS NULL", nativeQuery = true)
//    int countWritableReviews(@Param("memberId") String memberId);



    // 작성한 리뷰 조회
    @Query(value = "SELECT r.review_no, r.review_write_time, r.review_content, r.member_id, r.product_no, r.score, " +
            "p.product_name, p.product_image_link, rt.rental_state " +
            "FROM tbl_review r " +
            "LEFT JOIN tbl_product p ON r.product_no = p.product_no " +
            "LEFT JOIN tbl_rental rt ON r.product_no = rt.product_no AND r.member_id = rt.member_id " +
            "WHERE r.member_id = :memberId AND rt.rental_state = '예약완료' AND r.review_content IS NOT NULL " +
            "ORDER BY r.review_write_time DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Object[]> findWrittenReviews(@Param("memberId") String memberId, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM tbl_review r " +
            "LEFT JOIN tbl_rental rt ON r.product_no = rt.product_no AND r.member_id = rt.member_id " +
            "WHERE r.member_id = :memberId AND rt.rental_state = '예약완료' AND r.review_content IS NOT NULL", nativeQuery = true)
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
}

