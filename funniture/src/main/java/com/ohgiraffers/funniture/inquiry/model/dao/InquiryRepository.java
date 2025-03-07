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

//    @Query(value = "SELECT * FROM TBL_INQUIRY WHERE PRODUCT_NO = :productNo", nativeQuery = true)
//    List<InquiryEntity> findByProductNo(String productNo);
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


//    @Query(value = "select a.signup_date, a.phone_number, a.password, a.member_role, a.is_consulting, a.image_link, a.image_id, a.email, b.qna_write_time, a.user_name , a.member_id, b.inquiry_no, c.product_no, c.product_name, b.inquiry_content, b.qna_type from tbl_member a join tbl_inquiry b on (a.member_id = b.member_id) join tbl_product c on (a.member_id = c.owner_no)", nativeQuery = true)
//    List<MemberEntity> findAllInquiryOwnerPage();

//    @Query(value = """
//        select a.qna_write_time, b.user_name, b.member_id, a.inquiry_no, c.product_no, c.product_name, a.inquiry_content
//        from tbl_inquiry a join tbl_product c on (a.product_no = c.product_no)
//                           join tbl_member b on (a.member_id = b.member_id)
//        where c.owner_no = :ownerNo
//            """, nativeQuery = true)
//   List<InquiryEntity> findAllInquiryOwnerPage(String ownerNo);


    @Query(value = """
    SELECT i.*, 
           m.user_name AS userName,
           m.phone_number AS phoneNumber, 
           p.product_name AS productName,
           p.product_image_link AS productImageLink 
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
        WHERE p.owner_no = :ownerNo
        """, nativeQuery = true)
        int countAllInquiryOwnerPage(@Param("ownerNo") String ownerNo);


    @Query(value = """
        SELECT i.inquiry_no, i.member_id, i.inquiry_content, i.show_status, i.qna_type, i.product_no, i.qna_write_time, 
               m.user_name, m.phone_number, p.product_name, p.product_image_link 
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
