package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.CurrentMonthSalesDTO;
import com.ohgiraffers.funniture.rental.model.dto.MonthlySalesDTO;

import java.util.List;

public interface OwnerMonthlySalesRepositoryCustom {
    List<MonthlySalesDTO> getMonthlySales(String ownerNo, String yearMonth);
}
