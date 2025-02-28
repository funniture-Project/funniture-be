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
            builder.and(chatEntity.chatQaLevel.eq(1));
        }

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
