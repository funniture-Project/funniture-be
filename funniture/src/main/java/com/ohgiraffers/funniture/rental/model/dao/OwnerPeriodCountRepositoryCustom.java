package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.RentalPeriodCountDTO;

import java.util.List;

public interface OwnerPeriodCountRepositoryCustom {
    List<RentalPeriodCountDTO> countRentalsByPeriod(String ownerNo, String period);
}
