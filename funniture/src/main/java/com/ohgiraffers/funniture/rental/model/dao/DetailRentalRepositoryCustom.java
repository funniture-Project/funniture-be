package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.RentalDetailDTO;

import java.util.List;

public interface DetailRentalRepositoryCustom {
    List<RentalDetailDTO> findRentalDetail(String rentalNo);
}
