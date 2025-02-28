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

        System.out.println("질문 리스트 result = " + result);
    }
}
