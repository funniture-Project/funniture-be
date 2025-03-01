package com.ohgiraffers.funniture.rental.model.dao;


import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminRentalRepositoryCustom {
    Page<AdminRentalViewDTO> findRentalAllListByAdmin(AdminRentalSearchCriteria criteria, Pageable pageable);
}
