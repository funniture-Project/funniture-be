package com.ohgiraffers.funniture.chat.model.service;

import com.ohgiraffers.funniture.chat.entity.ChatEntity;
import com.ohgiraffers.funniture.chat.model.dao.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public void getChatQuList(Integer refNum, Integer qaLevel) {
        List<ChatEntity> result =  chatRepository.getChatQuList(refNum,qaLevel);
        if (qaLevel != null && qaLevel >= 2){
            List<ChatEntity> refResult = chatRepository.getRefChatQuList(qaLevel-1);
            System.out.println("상위 단계 중 하위질문이 있는 리스트 refResult = " + refResult);
        }

        System.out.println("해당 단계 질문 리스트 result = " + result);
    }
}
