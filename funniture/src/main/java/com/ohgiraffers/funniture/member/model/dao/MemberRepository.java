package com.ohgiraffers.funniture.member.model.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity , String> {
    // 마지막 멤버 넘버 가져오는 애
    @Query(value = "SELECT MAX(MEMBER_ID) FROM TBL_MEMBER",
            nativeQuery = true)
    String maxMemberNo();

//    MemberEntity findByMemberId(String memberId);

    MemberEntity findByEmail(String email);

    @Query(value = """
        SELECT DISTINCT
            oi.store_name,
            m.member_id
        FROM tbl_member m
        JOIN tbl_ownerinfo oi ON m.member_id = oi.member_id
        ORDER BY m.member_id
        """, nativeQuery = true)
    List<Object[]> findAllOwner();

    boolean existsByEmail(String email);

    MemberEntity findByMemberId(String memberId);


}
