package com.ohgiraffers.funniture.inquiry.model.dao;

import com.ohgiraffers.funniture.inquiry.entity.Inquiry;
import com.ohgiraffers.funniture.inquiry.entity.Member;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, String> {

    @Query(value = "SELECT MAX(INQUIRY_NO) FROM TBL_INQUIRY",
           nativeQuery = true)
    String maxInquiry();

    @Query(value = "SELECT * FROM TBL_INQUIRY WHERE PRODUCT_NO = :productNo", nativeQuery = true)
    List<Inquiry> findByProductNo(String productNo);

    @Query(value = "select a.signup_date, a.phone_number, a.password, a.member_role, a.is_consulting, a.image_link, a.image_id, a.email, b.qna_write_time, a.user_name , a.member_id, b.inquiry_no, c.product_no, c.product_name, b.inquiry_content, b.qna_type from tbl_member a join tbl_inquiry b on (a.member_id = b.member_id) join tbl_product c on (a.member_id = c.owner_no)", nativeQuery = true)
    List<Member> findAllInquiryOwnerPage();

}
