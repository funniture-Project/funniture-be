package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.CurrentMonthSalesDTO;

import java.util.List;

public interface OwnerCurrentMonthSalesRepositoryCustom {
    List<CurrentMonthSalesDTO> getCurrentMonthSales(String ownerNo, String yearMonth);
}
