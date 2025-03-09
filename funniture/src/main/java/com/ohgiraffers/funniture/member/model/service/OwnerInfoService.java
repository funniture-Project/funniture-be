package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import com.ohgiraffers.funniture.member.model.dao.OwnerRepository;
import com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerInfoService {

    private final OwnerRepository ownerRepository;
    private final ModelMapper modelMapper;

    public OwnerInfoDTO getOwnerInfo(String ownerNo) {
       OwnerInfoEntity owner = ownerRepository.findByMemberId(ownerNo).orElse(null);

        OwnerInfoDTO dtoData = modelMapper.map(owner, OwnerInfoDTO.class);

        return dtoData;
    }
}
