package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.ActiveRentalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserActiveRentalRepositoryCustom {
    Page<ActiveRentalDTO> findActiveRentalListByUser(String memberId, Pageable pageable);
}
