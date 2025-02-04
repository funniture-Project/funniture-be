package com.ohgiraffers.funniture.inquiry.model.service;

import com.ohgiraffers.funniture.inquiry.entity.Inquiry;
import com.ohgiraffers.funniture.inquiry.model.dao.InquiryRepository;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final ModelMapper modelMapper;
    private final InquiryRepository inquiryRepository;

    public List<InquiryDTO> findAllInquiry() {

        List<Inquiry> inquiryEntity = inquiryRepository.findAll();
        System.out.println("서비스 : 엔터티 inquiry = " + inquiryEntity);
        return inquiryEntity.stream().map(all -> modelMapper.map(all , InquiryDTO.class))
                .collect(Collectors.toList());
    }

    public InquiryDTO findByInqiryNo(String inquiryNo) {

        Inquiry result = inquiryRepository.findById(inquiryNo).orElseThrow();
        System.out.println("서비스에서 result = " + result);

        return modelMapper.map(result ,InquiryDTO.class);
    }

    @Transactional
    public void inquiryRegist(InquiryDTO inquiryDTO) {

        System.out.println("서비스에 잘 오는지inquiryDTO = " + inquiryDTO);

    inquiryRepository.save(modelMapper.map(inquiryDTO , Inquiry.class));

    }
}
