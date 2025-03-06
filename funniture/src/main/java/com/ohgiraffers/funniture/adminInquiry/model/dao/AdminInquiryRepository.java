package com.ohgiraffers.funniture.adminInquiry.model.dao;

import com.ohgiraffers.funniture.adminInquiry.entity.AdminInquiryEntity;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminInquiryRepository extends JpaRepository<AdminInquiryEntity, String> {

    @Query("""
        SELECT ai
        FROM AdminInquiryEntity ai
        WHERE ai.senderNo = :memberId or ai.receiveNo = :memberId
        ORDER BY ai.createDateTime
    """)
    List<AdminInquiryEntity> findByMemberId(String memberId);

    @Query(value = """
        SELECT m
        FROM MemberEntity m
        WHERE m.memberId in (
            SELECT DISTINCT ia.senderNo
            FROM AdminInquiryEntity ia
            WHERE ia.senderNo <> 'ADMIN'
            
            UNION 
            
            SELECT DISTINCT ia.receiveNo
            FROM AdminInquiryEntity ia
            WHERE ia.receiveNo <> 'ADMIN'
        )
        ORDER BY (
                SELECT MAX (ia.createDateTime)
                FROM AdminInquiryEntity ia
                WHERE ia.senderNo = m.memberId OR ia.receiveNo = m.memberId
        ) DESC 
    """)
    List<MemberEntity> getConsultingList();
}
