package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.AdminMonthlySalesDTO;

import java.util.List;

public interface SalesRepositoryCustom {
    List<AdminMonthlySalesDTO> getSales(String yearMonth, String groupBy);
}
