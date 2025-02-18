package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.OwnerRentalViewDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface OwnerRentalRepositoryCustom {
    List<OwnerRentalViewDTO> findRentalListByOwner(String ownerNo, String period);
}
