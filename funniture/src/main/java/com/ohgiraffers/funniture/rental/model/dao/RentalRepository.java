package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, String> {
}

