package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.AdminRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRentalRepository extends JpaRepository<AdminRentalEntity, String> {

    @Query("SELECT new com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO(" +
            "r.rentalNo, " +
            "o.storeName, " +
            "r.rentalState, " +
            "parentCategory.categoryName, " +
            "p.productName, " +
            "r.rentalStartDate, " +
            "r.rentalEndDate, " +
            "r.rentalNumber) " +
            "FROM AdminRentalEntity r " +
            "JOIN r.adminProduct p " +
            "JOIN p.adminOwnerInfo o " +
            "JOIN p.adminCategory c " +
            "LEFT JOIN c.refCategoryCode parentCategory")
    List<AdminRentalViewDTO> findRentalAllListByAdmin();

}
