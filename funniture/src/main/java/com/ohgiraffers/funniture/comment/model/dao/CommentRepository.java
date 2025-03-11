package com.ohgiraffers.funniture.comment.model.dao;

import com.ohgiraffers.funniture.comment.entity.CommentEntity;
import com.ohgiraffers.funniture.comment.model.dto.CommentByProductDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity , String> {

    @Query(value = "SELECT MAX(COMMENT_NO) FROM TBL_COMMENT",
            nativeQuery = true)
    Integer maxComment();

    // CommentRepository에 추가할 메서드
    @Query(value = "SELECT comment_level FROM tbl_comment WHERE comment_no = :parentCommentNo", nativeQuery = true)
    Integer getCommentLevel(int parentCommentNo);

    @Query(value = "SELECT comment_no, member_id, comment_write_time," +
            " comment_content, comment_level, parent_comment_no, inquiry_no" +
            " FROM tbl_comment WHERE inquiry_no = :inquiryNo", nativeQuery = true)
    CommentEntity findCommentByInquiryNo(String inquiryNo);


        @Query(value = """
        SELECT c.comment_no AS commentNo,
               c.member_id AS memberId,
               c.comment_write_time AS commentWriteTime,
               c.comment_content AS commentContent,
               c.comment_level AS commentLevel,
               c.parent_comment_no AS parentCommentNo,
               c.inquiry_no AS inquiryNo,
               o.store_name AS storeName
        FROM tbl_comment c
        LEFT JOIN tbl_member m ON c.member_id = m.member_id
        LEFT JOIN tbl_ownerinfo o ON m.member_id = o.member_id
        WHERE c.inquiry_no = :inquiryNo AND c.parent_comment_no = 0
        LIMIT 1
    """, nativeQuery = true)
        List<Object[]> findCommentByProductPage(@Param("inquiryNo") String inquiryNo);

}
