package com.ohgiraffers.funniture.point.model.dao;

import com.ohgiraffers.funniture.point.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, String> {
}
