package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface UserRentalRepositoryCustom {

    Page<UserOrderViewDTO> findRentalOrderListByUser(String memberId, String period, LocalDate searchDate, Pageable pageable);
}
