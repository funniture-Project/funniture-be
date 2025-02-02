package com.ohgiraffers.funniture.rental.model.service;

import com.ohgiraffers.funniture.rental.model.dao.RentalMapper;
import com.ohgiraffers.funniture.rental.model.dao.RentalRepository;
import com.ohgiraffers.funniture.rental.model.dto.RentalDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalMapper rentalMapper;
    private final RentalRepository rentalRepository;
    private final ModelMapper modelMapper;

    public List<RentalDTO> findRentalAllListByAdmin() {
        return rentalMapper.findRentalAllListByAdmin();
    }
}
