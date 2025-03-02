package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, String> {

    @Query("SELECT COUNT(r) FROM rental r WHERE DATE(r.orderDate) = :orderDateOnly")
    int countByOrderDate(@Param("orderDateOnly") LocalDate orderDateOnly);

    Optional<RentalEntity> findByRentalNo(String rentalNo);
}

