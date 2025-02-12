package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.common.ProductSearchCondition;
import com.ohgiraffers.funniture.product.entity.ProductWithPriceEntity;

import java.util.List;

public interface ProductWithPriceRepository{
    List<ProductWithPriceEntity> findSearchProductList(ProductSearchCondition condition);
}
