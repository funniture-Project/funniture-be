package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.AdminSalesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSalesRepositoryCustom {
    Page<AdminSalesDTO> findSalesByDate(String yearMonth, String storeName, Pageable pageable);
}
