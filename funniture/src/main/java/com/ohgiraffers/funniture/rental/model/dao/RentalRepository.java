package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, String> {

    @Query("SELECT COUNT(r) FROM rental r WHERE FUNCTION('DATE_FORMAT', r.orderDate, '%Y%m%d') = :orderDateOnly")
    int countByOrderDate(LocalDate orderDateOnly);

}

