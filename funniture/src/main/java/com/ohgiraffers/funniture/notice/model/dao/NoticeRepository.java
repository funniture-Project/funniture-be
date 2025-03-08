package com.ohgiraffers.funniture.notice.model.dao;

import com.ohgiraffers.funniture.notice.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity,Integer> {
}
