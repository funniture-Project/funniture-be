package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;

import java.time.LocalDate;
import java.util.List;

public interface UserRentalRepositoryCustom {
    List<UserOrderViewDTO> findRentalOrderListByUser(String period, LocalDate searchDate);
}
