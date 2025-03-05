package com.ohgiraffers.funniture.chat.model.service;

import com.ohgiraffers.funniture.chat.entity.ChatEntity;
import com.ohgiraffers.funniture.chat.model.dao.BasicChatRepository;
import com.ohgiraffers.funniture.chat.model.dao.ChatRepository;
import com.ohgiraffers.funniture.chat.model.dto.ChatDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final BasicChatRepository basicChatRepository;
    private final ModelMapper modelMapper;

    public List<ChatDTO> getChatQuList(Integer refNum, Integer qaLevel) {
        List<ChatEntity> result =  chatRepository.getChatQuList(refNum,qaLevel);

        return result.stream().map(item -> modelMapper.map(item, ChatDTO.class))
                .collect(Collectors.toList());
    }

    public List<ChatDTO> getRefChatQuList(Integer qaLevel) {
        List<ChatEntity> refResult = chatRepository.getRefChatQuList(qaLevel-1);

        return refResult.stream().map(item -> modelMapper.map(item, ChatDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean modifyChatList(List<ChatDTO> updateList) {
        System.out.println("전달 받은 updateList 입니다. = " + updateList);

        try{
            // 'new'로 시작하는 chatQaNo 필터링
            List<ChatDTO> newItems = updateList.stream()
                    .filter(item -> item.getChatQaNo() != null && item.getChatQaNo().startsWith("new"))
                    .collect(Collectors.toList());

            // 새로 저장
            if (newItems.size() > 0) {
                newItems.forEach(item -> {
                    item.setChatQaNo(null);
                });

                System.out.println("새로 저장될 챗봇 리스트 newItems = " + newItems);

                List<ChatEntity> newEntityItems = newItems.stream().map(item -> modelMapper.map(item, ChatEntity.class))
                                .collect(Collectors.toList());

                basicChatRepository.saveAll(newEntityItems);
            }

            // 기존꺼 업데이트
            if (updateList.size() != newItems.size()) {
                List<ChatDTO> existList = updateList.stream()
                        .filter(item -> item.getChatQaNo() != null && !item.getChatQaNo().startsWith("new"))
                        .collect(Collectors.toList());

                if (existList.size() > 0) {
                    existList.forEach(item -> {
                        ChatEntity existData = basicChatRepository.findById(Integer.parseInt(item.getChatQaNo())).orElse(null);
                        ChatEntity newItem = modelMapper.map(item, ChatEntity.class);

                        if (existData != null && !newItem.equals(existData)) {
                            System.out.println("update 대상 item = " + item);

                            existData = existData.toBuilder()
                                    .chatQaQuContent(item.getChatQaQuContent())
                                    .chatQaAnContent(item.getChatQaAnContent())
                                    .adminConnectAbsence(item.getAdminConnectAbsence())
                                    .nextQuAbsence(item.getNextQuAbsence())
                                    .refQuNo(item.getRefQuNo())
                                    .build();

                            System.out.println("수정된 existData = " + existData);

                            basicChatRepository.save(existData);
                        }
                    });
                }
            }
            return true;
        } catch (Exception e){
            System.out.println("ChatList 수정중 오류 발생 e = " + e);
            return false;
        }

    }
}
