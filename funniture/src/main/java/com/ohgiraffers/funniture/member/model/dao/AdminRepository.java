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

    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
            "IFNULL(b.current_point, 0) as current_point " +
            "FROM tbl_member a " +
            "LEFT JOIN tbl_point b ON a.member_id = b.member_id " +
            "WHERE a.member_role = 'USER'", nativeQuery = true)
    List<Object[]> AllUserListByAdmin();



    @Query(value = "SELECT a.member_id, a.store_no, a.store_name, a.store_phone, " +
            "b.user_name, b.email, b.signup_date, b.member_role " +
            "FROM tbl_ownerinfo a " +
            "JOIN tbl_member b ON a.member_id = b.member_id", nativeQuery = true)
    List<Object[]> findAllOwnerInfo();

}
