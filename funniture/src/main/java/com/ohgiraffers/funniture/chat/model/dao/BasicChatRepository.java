package com.ohgiraffers.funniture.chat.model.dao;

import com.ohgiraffers.funniture.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicChatRepository extends JpaRepository<ChatEntity, Integer> {
}
