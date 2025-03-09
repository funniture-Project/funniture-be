package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.OwnerSalesDTO;

import java.util.List;

public interface OwnerSalesRepositoryCustom {
    List<OwnerSalesDTO> getSalesByOwner(String ownerNo, String yearMonth, String productNo);
}
