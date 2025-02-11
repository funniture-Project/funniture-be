package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.product.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findByRefCategoryCode(Integer refCategoryCode);
}
