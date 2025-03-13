package com.ohgiraffers.funniture.inquiry.model.dao;

import com.ohgiraffers.funniture.inquiry.entity.InquiryEntity;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.funniture.inquiry.model.dto.MemberInquiryDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, String> {

    @Query(value = "SELECT MAX(INQUIRY_NO) FROM TBL_INQUIRY",
           nativeQuery = true)
    String maxInquiry();

    @Query(value = """
        SELECT i.*, 
               m.user_name AS userName,
               m.phone_number AS phoneNumber, 
               p.product_name AS productName,
               p.product_image_link AS productImageLink 
        FROM tbl_inquiry i
        LEFT JOIN tbl_member m ON i.member_id = m.member_id
        LEFT JOIN tbl_product p ON i.product_no = p.product_no
        WHERE i.product_no = :productNo
        """, nativeQuery = true)
    List<Object[]> findDetailedByProductNo(@Param("productNo") String productNo);

    // 대댓글 안 쓸 거니까 일단 c.parent_comment_no = 0 게 정의
    @Query(value = """
        SELECT i.*, 
               m.user_name AS userName,
               m.phone_number AS phoneNumber, 
               p.product_name AS productName,
               p.product_image_link AS productImageLink,
               CASE WHEN EXISTS (
                   SELECT 1 FROM tbl_comment c 
                   WHERE c.inquiry_no = i.inquiry_no AND c.parent_comment_no = 0
               ) THEN 'complete' ELSE 'waiting' END AS answerStatus
        FROM tbl_inquiry i
        LEFT JOIN tbl_member m ON i.member_id = m.member_id
        LEFT JOIN tbl_product p ON i.product_no = p.product_no
        WHERE p.owner_no = :ownerNo
        ORDER BY i.qna_write_time DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Object[]> findAllInquiryOwnerPage(@Param("ownerNo") String ownerNo, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = """
        SELECT COUNT(*) 
        FROM tbl_inquiry i
        LEFT JOIN tbl_product p ON i.product_no = p.product_no
        WHERE p.owner_no = :ownerNo AND NOT EXISTS (
            SELECT 1 FROM tbl_comment c 
            WHERE c.inquiry_no = i.inquiry_no AND c.parent_comment_no IS NULL
        )
    """, nativeQuery = true)
    int countAllInquiryOwnerPage(@Param("ownerNo") String ownerNo);

    @Query(value = """
        SELECT i.inquiry_no, i.member_id, i.inquiry_content, i.show_status, i.qna_type, i.product_no, i.qna_write_time, 
               m.user_name, m.phone_number, p.product_name, p.product_image_link,
               CASE WHEN EXISTS (
                   SELECT 1 FROM tbl_comment c 
                   WHERE c.inquiry_no = i.inquiry_no AND c.parent_comment_no = 0
               ) THEN 'complete' ELSE 'waiting' END AS answerStatus
        FROM tbl_inquiry i
        LEFT JOIN tbl_member m ON i.member_id = m.member_id
        LEFT JOIN tbl_product p ON i.product_no = p.product_no
        WHERE i.member_id = :memberId
        ORDER BY i.qna_write_time DESC
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Object[]> findAllInquiryUserPage(@Param("memberId") String memberId, @Param("limit") int limit, @Param("offset") int offset);


    @Query(value = """
        SELECT COUNT(*) 
        FROM tbl_inquiry i
        WHERE i.member_id = :memberId
        """, nativeQuery = true)
    int countAllInquiryUserPage(@Param("memberId") String memberId);


}
