package com.ohgiraffers.funniture.adminInquiry.model.dao;

import com.ohgiraffers.funniture.adminInquiry.entity.AdminInquiryEntity;
import com.ohgiraffers.funniture.adminInquiry.entity.QAdminInquiryEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
@RequiredArgsConstructor
public class AdminInquiryDSL{
//    public class AdminInquiryDSL implements AdminInquiryRepository{
    private final JPAQueryFactory jpaQueryFactory;

//    @Override
//    public List<AdminInquiryEntity> findByMemberId(String memberId) {
//        System.out.println("memberId = " + memberId);
//
//        QAdminInquiryEntity adminInquiry = QAdminInquiryEntity.adminInquiryEntity;
//
//        BooleanBuilder builder = new BooleanBuilder();
//
//        if (memberId != null || memberId != ""){
//            builder.and(adminInquiry.senderNo.eq(memberId))
//                    .or(adminInquiry.receiveNo.eq(memberId));
//        }
//
//
//
//    }
}
