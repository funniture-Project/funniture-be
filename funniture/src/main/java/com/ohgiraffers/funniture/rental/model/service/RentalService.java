package com.ohgiraffers.funniture.rental.model.service;

import com.ohgiraffers.funniture.rental.model.dao.RentalMapper;
import com.ohgiraffers.funniture.rental.model.dao.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalMapper rentalMapper;
    private final RentalRepository rentalRepository;
    private final ModelMapper modelMapper;
}
