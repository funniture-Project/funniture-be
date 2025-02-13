package com.ohgiraffers.funniture.inquiry.model.dao;

import com.ohgiraffers.funniture.inquiry.entity.InquiryEntity;
import com.ohgiraffers.funniture.inquiry.model.dto.OwnerInquiryDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, String> {

    @Query(value = "SELECT MAX(INQUIRY_NO) FROM TBL_INQUIRY",
            nativeQuery = true)
    String maxInquiry();

    @Query(value = "SELECT * FROM TBL_INQUIRY WHERE PRODUCT_NO = :productNo", nativeQuery = true)
    List<InquiryEntity> findByProductNo(String productNo);

//    @Query(value = "select a.signup_date, a.phone_number, a.password, a.member_role, a.is_consulting, a.image_link, a.image_id, a.email, b.qna_write_time, a.user_name , a.member_id, b.inquiry_no, c.product_no, c.product_name, b.inquiry_content, b.qna_type from tbl_member a join tbl_inquiry b on (a.member_id = b.member_id) join tbl_product c on (a.member_id = c.owner_no)", nativeQuery = true)
//    List<MemberEntity> findAllInquiryOwnerPage();

//    @Query(value = """
//        select a.qna_write_time, b.user_name, b.member_id, a.inquiry_no, c.product_no, c.product_name, a.inquiry_content
//        from tbl_inquiry a join tbl_product c on (a.product_no = c.product_no)
//                           join tbl_member b on (a.member_id = b.member_id)
//        where c.owner_no = :ownerNo
//            """, nativeQuery = true)
//   List<InquiryEntity> findAllInquiryOwnerPage(String ownerNo);

    @Query("""
        SELECT new com.ohgiraffers.funniture.inquiry.model.dto.OwnerInquiryDTO(
            a.qnaWriteTime,
            b.userName,
            b.memberId,
            a.inquiryNo,
            a.productNo, 
            c.productName,
            a.inquiryContent
        )
        FROM InquiryEntity a
        JOIN ProductEntity c ON a.productNo = c.productNo
        JOIN MemberEntity b ON a.memberId = b.memberId
        WHERE c.ownerNo = :ownerNo
        """)
    List<OwnerInquiryDTO> findAllInquiryOwnerPage(@Param("ownerNo") String ownerNo);

}