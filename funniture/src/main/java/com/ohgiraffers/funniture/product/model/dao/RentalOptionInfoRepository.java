package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.RentalOptionInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalOptionInfoRepository extends JpaRepository<RentalOptionInfoEntity, Integer> {
}
