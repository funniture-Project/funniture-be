package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.RentalStateCountDTO;

import java.util.List;

public interface UserRentalStateCountRepositoryCustom {
    List<RentalStateCountDTO> countRentalStatesByUser(String memberId);
}
