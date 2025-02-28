package com.ohgiraffers.funniture.chat.model.dao;

import com.ohgiraffers.funniture.chat.entity.ChatEntity;
import com.ohgiraffers.funniture.chat.entity.QChatEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatDSL implements ChatRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ConversionService conversionService;

    @Override
    public List<ChatEntity> getChatQuList(Integer refNum, Integer qaLevel) {

        System.out.println("검색 조건 refNum : " + refNum + " , qaLevel : " + qaLevel);

        QChatEntity chatEntity = QChatEntity.chatEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if (refNum != null){
            builder.and(chatEntity.refQuNo.eq(refNum));
        }

        if (qaLevel != null){
            builder.and(chatEntity.chatQaLevel.eq(qaLevel));
        } else {
            // 관리자 페이지에서 단계별 리스트 요청할때 (1단계 전체 요청)
            if (refNum == null){
                builder.and(chatEntity.chatQaLevel.eq((1)));
            }
        }
        System.out.println("builder = " + builder);

        return jpaQueryFactory
                .select(Projections.bean(ChatEntity.class,
                        chatEntity.chatQaNo,
                        chatEntity.chatQaLevel,
                        chatEntity.chatQaQuContent,
                        chatEntity.chatQaAnContent,
                        chatEntity.nextQuAbsence,
                        chatEntity.adminConnectAbsence,
                        chatEntity.refQuNo
                        ))
                .from(chatEntity)
                .where(builder)
                .fetch();
    }

    @Override
    public List<ChatEntity> getRefChatQuList(int i) {
        System.out.println("현재에서 상위는 몇단계?" + i);

        QChatEntity chatEntity = QChatEntity.chatEntity;

        BooleanBuilder builder = new BooleanBuilder();

        // 바로 윗단계질문인지 확인
        builder.and(chatEntity.chatQaLevel.eq(i));

        // 다음 질문이 있다고 답한 데이터만 출력
        builder.and(chatEntity.nextQuAbsence.isTrue());

        return jpaQueryFactory
                .select(Projections.bean(ChatEntity.class,
                        chatEntity.chatQaNo,
                        chatEntity.chatQaLevel,
                        chatEntity.chatQaQuContent,
                        chatEntity.chatQaAnContent,
                        chatEntity.nextQuAbsence,
                        chatEntity.adminConnectAbsence,
                        chatEntity.refQuNo
                ))
                .from(chatEntity)
                .where(builder)
                .fetch();
    }
}
