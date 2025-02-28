package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.ProductSearchCondition;
import com.ohgiraffers.funniture.product.entity.ProductWithPriceEntity;
import com.ohgiraffers.funniture.product.model.dto.RecentProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductWithPriceRepository{
    List<ProductWithPriceEntity> findSearchProductList(ProductSearchCondition condition);

    List<RecentProductDTO> findAllProductInfo(List<String> productList);

    Page<ProductWithPriceEntity> findSearchPagingProductList(ProductSearchCondition condition, Criteria cri);
}
