package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.model.dto.RentalDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RentalMapper {

    List<RentalDTO> findRentalAllListByAdmin();
}
