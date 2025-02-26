package com.ohgiraffers.funniture.member.model.dao;

import com.ohgiraffers.funniture.member.entity.MemberAndPointEntity;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<MemberAndPointEntity, String> {

    @Query(value = "select a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, b.current_point from tbl_member a join tbl_point b on a.member_id = b.member_id", nativeQuery = true)
    List<MemberAndPointEntity> AllUserListByAdmin();
}
