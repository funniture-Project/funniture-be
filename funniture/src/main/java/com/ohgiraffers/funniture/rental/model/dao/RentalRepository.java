package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, String> {

    @Query("SELECT COUNT(r) FROM rental r WHERE DATE(r.orderDate) = :orderDateOnly")
    int countByOrderDate(@Param("orderDateOnly") LocalDate orderDateOnly);

    Optional<RentalEntity> findByRentalNo(String rentalNo);

    @Query(value = "SELECT rt.rental_no" +
            " , rt.order_date" +
            " , p.product_no" +
            " , p.product_name" +
            " , p.product_image_link " +
            " , roi.rental_term" +
            " , roi.rental_price " +
            "FROM tbl_rental rt " +
            "LEFT JOIN tbl_product p " +
            " ON rt.product_no = p.product_no " +
            "LEFT JOIN tbl_rentaloptioninfo roi " +
            " ON rt.rental_info_no = roi.rental_info_no " +
            " AND rt.product_no = roi.product_no " +
            "LEFT JOIN tbl_review r " +
            " ON rt.product_no = r.product_no " +
            " AND rt.member_id = r.member_id " + // 수정된 조인 조건
            "WHERE rt.member_id = :memberId" +
            " AND rt.rental_state IN ('배송완료', '반납요청', '수거중', '반납완료') " + // 여러 상태 필터링
            " AND r.review_no IS NULL " + // 조건: 예약완료 + 리뷰 없음
            "ORDER BY rt.order_date DESC " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Object[]> findWritableReviews(@org.apache.ibatis.annotations.Param("memberId") String memberId, @org.apache.ibatis.annotations.Param("limit") int limit, @org.apache.ibatis.annotations.Param("offset") int offset);


    @Query(value = "SELECT COUNT(*) FROM tbl_rental rt " +
            "LEFT JOIN tbl_review r ON rt.product_no = r.product_no " +
            " AND rt.member_id = r.member_id " +
            "WHERE rt.member_id = :memberId" +
            " AND rt.rental_state IN ('배송완료', '반납요청', '수거중', '반납완료') " + // 여러 상태 필터링
            " AND r.review_no IS NULL", nativeQuery = true)
    int countWritableReviews(@org.apache.ibatis.annotations.Param("memberId") String memberId);



}

