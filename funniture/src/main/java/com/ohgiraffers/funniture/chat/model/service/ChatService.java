package com.ohgiraffers.funniture.chat.model.service;

import com.ohgiraffers.funniture.chat.entity.ChatEntity;
import com.ohgiraffers.funniture.chat.model.dao.ChatRepository;
import com.ohgiraffers.funniture.chat.model.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;

    public List<ChatDTO> getChatQuList(Integer refNum, Integer qaLevel) {
        List<ChatEntity> result =  chatRepository.getChatQuList(refNum,qaLevel);

        System.out.println("해당 단계 질문 리스트 result = " + result);

        return result.stream().map(item -> modelMapper.map(item, ChatDTO.class))
                .collect(Collectors.toList());
    }

    public List<ChatDTO> getRefChatQuList(Integer qaLevel) {
        List<ChatEntity> refResult = chatRepository.getRefChatQuList(qaLevel-1);
        System.out.println("상위 단계 중 하위질문이 있는 리스트 refResult = " + refResult);

        return refResult.stream().map(item -> modelMapper.map(item, ChatDTO.class))
                .collect(Collectors.toList());
    }
}
