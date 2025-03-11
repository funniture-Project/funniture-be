package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.RentalStateCountDTO;

import java.util.List;

public interface OwnerRentalStateCountRepositoryCustom {
    List<RentalStateCountDTO> countRentalStatesByOwner(String ownerNo);
}
