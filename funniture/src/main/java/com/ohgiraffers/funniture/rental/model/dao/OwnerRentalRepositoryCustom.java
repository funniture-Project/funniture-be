package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.OwnerRentalViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface OwnerRentalRepositoryCustom {

    Page<OwnerRentalViewDTO> findRentalListByOwner(String ownerNo, String period, String rentalTab, Pageable pageable);
}
