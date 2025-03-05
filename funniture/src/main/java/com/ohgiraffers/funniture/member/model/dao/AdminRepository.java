package com.ohgiraffers.funniture.member.model.dao;

import com.ohgiraffers.funniture.member.entity.MemberAndPointEntity;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import com.ohgiraffers.funniture.member.model.dto.AppOwnerListModalDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<MemberAndPointEntity, String> {


    // 관리자 페이지에서 전체 회원 정보 가져오는 로직 (이렇게 해야 포인트 수정에 따른 결과 하나씩 출력)
//    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
//            "IFNULL(b.current_point, 0) AS current_point, o.is_rejected " +
//            "FROM tbl_member a " +
//            "LEFT JOIN (SELECT member_id, current_point " +
//            "           FROM tbl_point " +
//            "           WHERE (member_id, point_date_time) IN (SELECT member_id, MAX(point_date_time) " +
//            "                                                  FROM tbl_point " +
//            "                                                  GROUP BY member_id)) b ON a.member_id = b.member_id " +
//            "LEFT JOIN tbl_ownerinfo o ON a.member_id = o.member_id " +
//            "WHERE a.member_role = 'USER' AND (o.is_rejected IS NULL OR o.is_rejected = -1) " +
//            "ORDER BY a.member_id ASC",
//            nativeQuery = true)
//    List<Object[]> AllUserListByAdmin();

    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
            "IFNULL(b.current_point, 0) AS current_point, o.is_rejected " +
            "FROM tbl_member a " +
            "LEFT JOIN (SELECT member_id, current_point " +
            "           FROM tbl_point " +
            "           WHERE (member_id, point_date_time) IN (SELECT member_id, MAX(point_date_time) " +
            "                                                  FROM tbl_point " +
            "                                                  GROUP BY member_id)) b ON a.member_id = b.member_id " +
            "LEFT JOIN tbl_ownerinfo o ON a.member_id = o.member_id " +
            "WHERE a.member_role = 'USER' AND (o.is_rejected IS NULL OR o.is_rejected = -1) " +
            "ORDER BY a.member_id ASC " +
            "LIMIT :offset, :size",
            nativeQuery = true)
    List<Object[]> AllUserListByAdmin(@Param("offset") int offset, @Param("size") int size);

    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_member a " +
            "LEFT JOIN tbl_ownerinfo o ON a.member_id = o.member_id " +
            "WHERE a.member_role = 'USER' AND (o.is_rejected IS NULL OR o.is_rejected = -1)",
            nativeQuery = true)
    int countAllUsers();

    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
            "IFNULL(b.current_point, 0) AS current_point " +
            "FROM tbl_member a " +
            "LEFT JOIN (SELECT member_id, current_point " +
            "           FROM tbl_point " +
            "           WHERE (member_id, point_date_time) IN (SELECT member_id, MAX(point_date_time) " +
            "                                                  FROM tbl_point " +
            "                                                  GROUP BY member_id)) b ON a.member_id = b.member_id " +
            "WHERE a.member_role = 'LIMIT' " +
            "ORDER BY a.member_id ASC " +
            "LIMIT :offset, :size",
            nativeQuery = true)
    List<Object[]> AllLeaverListByAdmin(@Param("offset") int offset, @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM tbl_member WHERE member_role = 'LIMIT'", nativeQuery = true)
    int countAllLeavers();


    @Query(value = "SELECT a.member_id, a.store_no, a.store_name, a.store_phone, " +
            "b.user_name, b.email, b.signup_date, b.member_role " +
            "FROM tbl_ownerinfo a " +
            "JOIN tbl_member b ON a.member_id = b.member_id " +
            "WHERE a.is_rejected = 1 " +
            "ORDER BY a.member_id ASC " +
            "LIMIT :offset, :size", nativeQuery = true)
    List<Object[]> findAllOwnerInfo(@Param("offset") int offset, @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM tbl_ownerinfo WHERE is_rejected = 1", nativeQuery = true)
    int countAllOwners();



    @Query(value = "SELECT m.member_id, m.user_name, m.phone_number, m.email, m.signup_date, m.member_role, o.is_rejected " +
            "FROM tbl_member m " +
            "JOIN tbl_ownerinfo o ON m.member_id = o.member_id " +
            "WHERE o.is_rejected = 0 " +
            "ORDER BY m.member_id ASC " +
            "LIMIT :offset, :size",
            nativeQuery = true)
    List<Object[]> AllConvertListByAdmin(@Param("offset") int offset, @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM tbl_member m " +
            "JOIN tbl_ownerinfo o ON m.member_id = o.member_id " +
            "WHERE o.is_rejected = 0",
            nativeQuery = true)
    int countAllConvertApps();



//    @Query(value = "SELECT a.member_id, a.user_name, a.phone_number, a.email, a.signup_date, a.member_role, " +
//            "IFNULL(b.current_point, 0) AS current_point " +
//            "FROM tbl_member a " +
//            "LEFT JOIN (SELECT member_id, current_point " +
//            "           FROM tbl_point " +
//            "           WHERE (member_id, point_date_time) IN (SELECT member_id, MAX(point_date_time) " +
//            "                                                  FROM tbl_point " +
//            "                                                  GROUP BY member_id)) b ON a.member_id = b.member_id " +
//            "WHERE a.member_role = 'LIMIT' " +
//            "ORDER BY a.member_id ASC",
//            nativeQuery = true)
//    List<Object[]> AllLeaverListByAdmin();


    @Query(value = "SELECT new com.ohgiraffers.funniture.member.model.dto.AppOwnerListModalDTO(" +
            "m.memberId, m.userName, m.phoneNumber, m.email, m.signupDate, m.memberRole, " +
            "new com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO(" +
            "o.storeNo, o.memberId, o.storeName, o.storeAddress, o.account, o.bank, o.attechmentLink, " +
            "o.isRejected, o.storeImage, o.storePhone)) " +
            "FROM MemberAndPointEntity m " +
            "LEFT JOIN OwnerInfoEntity o ON m.memberId = o.memberId " +
            "WHERE m.memberId = :memberId AND o.isRejected = 0")
    AppOwnerListModalDTO findConvertAppDetailByMemberId(@Param("memberId") String memberId);

    @Query(value = "SELECT new com.ohgiraffers.funniture.member.model.dto.AppOwnerListModalDTO(" +
            "m.memberId, m.userName, m.phoneNumber, m.email, m.signupDate, m.memberRole, " +
            "new com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO(" +
            "o.storeNo, o.memberId, o.storeName, o.storeAddress, o.account, o.bank, o.attechmentLink, " +
            "o.isRejected, o.storeImage, o.storePhone)) " +
            "FROM MemberAndPointEntity m " +
            "LEFT JOIN OwnerInfoEntity o ON m.memberId = o.memberId " +
            "WHERE m.memberId = :memberId AND o.isRejected = 1")
    AppOwnerListModalDTO findOwnerDetailByMemberId(@Param("memberId") String memberId);

}

//    @Query("SELECT m.memberId, m.userName, m.phoneNumber, m.email, m.signupDate, c.attachmentLink " +
//            "FROM Member m JOIN ConvertApplication c ON m.memberId = c.member.memberId " +
//            "WHERE m.memberId = :memberId AND c.isResult = 0")
//    Object[] getConvertDetailByAdmin(@Param("memberId") Long memberId);



//    @Query("SELECT new com.ohgiraffers.funniture.member.model.dto.AppOwnerListModalDTO(" +
//            "m.memberId, m.userName, m.phoneNumber, m.email, m.signupDate, m.memberRole, " +
//            "new com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO(" +
//            "o.storeNo, o.memberId, o.storeName, o.storeAddress, o.account, o.bank, o.attechmentLink, " +
//            "o.isRejected, o.storeImage, o.storePhone)) " +
//            "FROM MemberAndPointEntity m " +
//            "JOIN OwnerInfoEntity o ON m.memberId = o.memberId " +
//            "WHERE o.isRejected = 0")
//    List<AppOwnerListModalDTO> findConvertAppListByAdminModal();

