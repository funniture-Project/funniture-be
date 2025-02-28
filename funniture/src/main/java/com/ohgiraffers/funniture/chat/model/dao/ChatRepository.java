package com.ohgiraffers.funniture.chat.model.dao;

import com.ohgiraffers.funniture.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository {
    List<ChatEntity> getChatQuList(Integer refNum, Integer qaLevel);
}
