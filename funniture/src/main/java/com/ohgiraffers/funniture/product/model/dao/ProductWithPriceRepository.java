package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.common.ProductSearchCondition;
import com.ohgiraffers.funniture.product.entity.ProductWithPriceEntity;
import com.ohgiraffers.funniture.product.model.dto.RecentProductDTO;

import java.util.List;

public interface ProductWithPriceRepository{
    List<ProductWithPriceEntity> findSearchProductList(ProductSearchCondition condition);

    List<RecentProductDTO> findAllProductInfo(List<String> productList);
}
