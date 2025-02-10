package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.UserRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRentalRepository extends JpaRepository<UserRentalEntity, String> {

    @Query("SELECT new com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO(" +
            "r.rentalNo, " +
            "r.orderDate, " +
            "r.rentalState, " +
            "p.productName, " +
            "i.rentalPrice) " +
            "FROM UserRentalEntity r " +
            "JOIN r.productEntity p " +
            "JOIN r.rentalOptionInfoEntity i")
    List<UserOrderViewDTO> findRentalOrderListByUser();
}
