package com.ohgiraffers.funniture.review.model.dao;

import com.ohgiraffers.funniture.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity , String> {

    @Query(value = "SELECT MAX(REVIEW_NO) FROM TBL_REVIEW",
            nativeQuery = true)
    String maxReview();

}
