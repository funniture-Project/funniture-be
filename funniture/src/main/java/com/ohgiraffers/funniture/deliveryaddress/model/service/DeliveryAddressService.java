package com.ohgiraffers.funniture.deliveryaddress.model.service;

import com.ohgiraffers.funniture.deliveryaddress.entity.DeliveryAddressEntity;
import com.ohgiraffers.funniture.deliveryaddress.model.dao.DeliveryAddressRepository;
import com.ohgiraffers.funniture.deliveryaddress.model.dto.DeliveryAddressDTO;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final ModelMapper modelMapper;

    public List<DeliveryAddressDTO> findDeliveryAddressByUser(String memberId) {

        List<DeliveryAddressEntity> addressList = deliveryAddressRepository.findDeliveryAddressByUser(memberId);

        return addressList.stream().map(address -> modelMapper.map(address, DeliveryAddressDTO.class)).collect(Collectors.toList());
    }
}
