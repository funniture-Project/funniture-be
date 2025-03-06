package com.ohgiraffers.funniture.adminInquiry.model.service;

import com.ohgiraffers.funniture.adminInquiry.entity.AdminInquiryEntity;
import com.ohgiraffers.funniture.adminInquiry.model.dao.AdminInquiryRepository;
import com.ohgiraffers.funniture.adminInquiry.model.dto.AdminInquiryDTO;
import com.ohgiraffers.funniture.adminInquiry.model.dto.ConsultingListDTO;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInquiryService {

    private final AdminInquiryRepository adminInquiryRepository;
    private final ModelMapper modelMapper;

    public List<AdminInquiryDTO> getListById(String memberId) {
        List<AdminInquiryEntity> inquiryList =  adminInquiryRepository.findByMemberId(memberId);

        List<AdminInquiryDTO> inquiryDTOList = inquiryList.stream().map(item -> modelMapper.map(item, AdminInquiryDTO.class))
                .collect(Collectors.toList());

        return inquiryDTOList;
    }

    public List<ConsultingListDTO> getConsultingList() {

        List<MemberEntity> memberList = adminInquiryRepository.getConsultingList();

        List<ConsultingListDTO> consultingList = memberList.stream().map(item -> modelMapper.map(item, ConsultingListDTO.class))
                                                .collect(Collectors.toList());

        return consultingList;
    }
}
