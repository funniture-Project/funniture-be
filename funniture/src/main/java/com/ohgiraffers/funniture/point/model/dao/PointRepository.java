package com.ohgiraffers.funniture.point.model.dao;

import com.ohgiraffers.funniture.point.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, String> {

    @Query("SELECT p.currentPoint FROM point p WHERE p.memberId = :memberId ORDER BY p.pointDateTime DESC LIMIT 1")
    int findCurrentPointByUser(String memberId);

}
