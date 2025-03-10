package com.ohgiraffers.funniture.member.model.dao;

import com.ohgiraffers.funniture.member.entity.CountCombinedKey;
import com.ohgiraffers.funniture.member.entity.CountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountRepository extends JpaRepository<CountEntity, CountCombinedKey> {

    @Query("SELECT c FROM CountEntity c WHERE FUNCTION('DATE_FORMAT', c.connectDate, '%Y-%m') = :yearMonth")
    List<CountEntity> getCountByMonth(@Param("yearMonth") String yearMonth);
}
