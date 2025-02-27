package com.ohgiraffers.funniture.member.model.dao;

import com.ohgiraffers.funniture.member.entity.MemberAndPointEntity;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<MemberAndPointEntity, String> {

//    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
//            "IFNULL(b.current_point, 0) AS current_point " +
//            "FROM tbl_member a " +
//            "LEFT JOIN tbl_point b ON a.member_id = b.member_id " +
//            "LEFT JOIN tbl_ownerinfo o ON a.member_id = o.member_id " +  // ✅ ownerInfo 테이블 조인
//            "WHERE a.member_role = 'USER' " +
//            "AND o.is_rejected = -1", nativeQuery = true)  // ✅ is_rejected가 -1인 유저만 조회
//    List<Object[]> AllUserListByAdmin();
//    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
//            "IFNULL(b.current_point, 0) as current_point " +
//            "FROM tbl_member a " +
//            "LEFT JOIN tbl_point b ON a.member_id = b.member_id " +
//            "WHERE a.member_role = 'USER'", nativeQuery = true)
//    List<Object[]> AllUserListByAdmin();
//    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
//            "IFNULL(b.current_point, 0) AS current_point " +
//            "FROM tbl_member a " +
//            "LEFT JOIN tbl_point b ON a.member_id = b.member_id " +
//            "LEFT JOIN tbl_ownerinfo o ON a.member_id = o.member_id " +  // ✅ ownerInfo 테이블 조인
//            "WHERE a.member_role = 'USER' " +
//            "AND IFNULL(o.is_rejected, -1) = -1", nativeQuery = true)  // ✅ is_rejected가 -1이거나 NULL인 유저 포함
//    List<Object[]> AllUserListByAdmin();

    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
            "IFNULL(b.current_point, 0) AS current_point " +
            "FROM tbl_member a " +
            "LEFT JOIN tbl_point b ON a.member_id = b.member_id " +
            "LEFT JOIN tbl_ownerinfo o ON a.member_id = o.member_id " +
            "WHERE a.member_role = 'USER' " +
            "ORDER BY CASE WHEN o.is_rejected = -1 THEN 0 ELSE 1 END, " +
            "a.member_id ASC",
            nativeQuery = true)
    List<Object[]> AllUserListByAdmin();




    @Query(value = "SELECT a.member_id, a.store_no, a.store_name, a.store_phone, " +
            "b.user_name, b.email, b.signup_date, b.member_role " +
            "FROM tbl_ownerinfo a " +
            "JOIN tbl_member b ON a.member_id = b.member_id " +
            "WHERE a.is_rejected = 1", nativeQuery = true)
    List<Object[]> findAllOwnerInfo();



    @Query(value = "SELECT m.member_id, m.user_name, m.phone_number, m.email, m.signup_date, m.member_role, o.is_rejected " +
            "FROM tbl_member m " +
            "JOIN tbl_ownerinfo o ON m.member_id = o.member_id " +
            "WHERE o.is_rejected = 0",
            nativeQuery = true)
    List<Object[]> AllConvertListByAdmin();


}
