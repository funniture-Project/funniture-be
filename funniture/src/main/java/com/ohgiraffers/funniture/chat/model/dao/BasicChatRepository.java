package com.ohgiraffers.funniture.chat.model.dao;

import com.ohgiraffers.funniture.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicChatRepository extends JpaRepository<ChatEntity, Integer> {

    @Query("select c from ChatEntity c where c.refQuNo = :chatNo")
    List<ChatEntity> findAllByRefNo(Integer chatNo);

    void deleteAllByRefQuNo(Integer chatNo);
}
