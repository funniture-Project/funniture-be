package com.ohgiraffers.funniture.point.model.dao;

import com.ohgiraffers.funniture.point.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, Integer> {

    @Query("SELECT SUM(ph.usedPoint) FROM pointHistory ph WHERE ph.memberId = :memberId")
    int sumUsedPointsByMemberId(String memberId);
}
