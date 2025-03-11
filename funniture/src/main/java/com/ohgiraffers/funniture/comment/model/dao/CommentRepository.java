package com.ohgiraffers.funniture.comment.model.dao;

import com.ohgiraffers.funniture.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
