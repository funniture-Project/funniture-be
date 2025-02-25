package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.RentalOptionInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalOptionInfoRepository extends JpaRepository<RentalOptionInfoEntity, Integer> {
    List<RentalOptionInfoEntity> findAllByProductNo(String productNo);
}
