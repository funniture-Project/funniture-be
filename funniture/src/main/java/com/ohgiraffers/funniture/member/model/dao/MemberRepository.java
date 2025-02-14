package com.ohgiraffers.funniture.member.model.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity , String> {
    // 마지막 멤버 넘버 가져오는 애
    @Query(value = "SELECT MAX(MEMBER_ID) FROM TBL_MEMBER",
            nativeQuery = true)
    String maxMemberNo();

}
