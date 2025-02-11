package com.ohgiraffers.funniture.rental.model.dao;


import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalSearchCriteria;

import java.util.List;

public interface AdminRentalRepositoryCustom {
    List<AdminRentalViewDTO> findRentalAllListByAdmin(AdminRentalSearchCriteria criteria);
}
