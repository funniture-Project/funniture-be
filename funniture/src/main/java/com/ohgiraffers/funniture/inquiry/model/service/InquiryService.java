package com.ohgiraffers.funniture.inquiry.model.service;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.PageDTO;
import com.ohgiraffers.funniture.common.PagingResponseDTO;
import com.ohgiraffers.funniture.inquiry.entity.InquiryEntity;
import com.ohgiraffers.funniture.inquiry.model.dao.InquiryRepository;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.funniture.inquiry.model.dto.MemberInquiryDTO;
import com.ohgiraffers.funniture.inquiry.model.dto.OwnerInquiryDTO;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final ModelMapper modelMapper;
    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    public List<InquiryDTO> findAllInquiry() {

        List<InquiryEntity> inquiryEntity = inquiryRepository.findAll();
        return inquiryEntity.stream().map(all -> modelMapper.map(all , InquiryDTO.class))
                .collect(Collectors.toList());
    }

    public InquiryDTO findByInqiryNo(String inquiryNo) {

        InquiryEntity result = inquiryRepository.findById(inquiryNo).orElseThrow();

        return modelMapper.map(result ,InquiryDTO.class);
    }

    @Transactional
    public void inquiryRegist(InquiryDTO inquiryDTO) {

        LocalDateTime currentTime = LocalDateTime.now();
        inquiryDTO.setQnaWriteTime(currentTime);

        // memberId를 이용해 회원 정보 조회
        MemberEntity memberEntity = memberRepository.findById(inquiryDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        inquiryRepository.save(modelMapper.map(inquiryDTO , InquiryEntity.class));
    }

    public String getMaxInquiry() {

        String maxIn = inquiryRepository.maxInquiry();

        return maxIn;
    }

    @Transactional
    public void modifyByInquiryNo(String inquiryNo, InquiryDTO inquiryDTO) {

        InquiryEntity result = inquiryRepository.findById(inquiryNo).orElseThrow();

        inquiryRepository.save(result);

    }

    @Transactional
    public void deleteByInquiryNo(String inquiryNo) {

        inquiryRepository.deleteById(inquiryNo);
    }


    public List<InquiryDTO> findByProductNo(String productNo) {
        List<Object[]> results = inquiryRepository.findDetailedByProductNo(productNo);

        return results.stream().map(obj -> new InquiryDTO(
                (String) obj[0],  // inquiryNo
                (String) obj[1],  // memberId
                (String) obj[2],  // inquiryContent
                (Integer) obj[3], // showStatus
                (Integer) obj[4], // qnaType
                (String) obj[5],  // productNo
                ((Timestamp) obj[6]).toLocalDateTime(), // qnaWriteTime
                (String) obj[7],  // userName (from tbl_member)
                (String) obj[8],  // productName (from tbl_product)
                (String) obj[9],   // phoneNumber (from tbl_member)
                (String) obj[10]   // productImageLink (from tbl_product)
        )).collect(Collectors.toList());
    }


    public PagingResponseDTO findByInquiryOwnerPage(String ownerNo, Criteria cri) {
        int offset = (cri.getPageNum() - 1) * cri.getAmount();
        int limit = cri.getAmount();
        List<Object[]> results = inquiryRepository.findAllInquiryOwnerPage(ownerNo, limit, offset);


        int total = inquiryRepository.countAllInquiryOwnerPage(ownerNo);

        List<OwnerInquiryDTO> dtos = results.stream().map(obj -> new OwnerInquiryDTO(
                (String) obj[0],  // inquiryNo
                (String) obj[1],  // memberId
                (String) obj[2],  // inquiryContent
                (Integer) obj[3], // showStatus
                (Integer) obj[4], // qnaType
                (String) obj[5],  // productNo
                ((Timestamp) obj[6]).toLocalDateTime(), // qnaWriteTime
                (String) obj[7],  // userName (from tbl_member)
                (String) obj[8],  // phoneNumber (from tbl_member)
                (String) obj[9],  // productName (from tbl_product)
                (String) obj[10],  // productImageLink (from tbl_product)
                (String) obj[11]  // answerStatus
        )).collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO response = new PagingResponseDTO();
        response.setData(dtos);
        response.setPageInfo(pageInfo);

        return response;
    }

    public PagingResponseDTO findByInquiryUserPage(String memberId, Criteria cri) {

        int offset = (cri.getPageNum() - 1) * cri.getAmount();
        int limit = cri.getAmount();

        List<Object[]> results = inquiryRepository.findAllInquiryUserPage(memberId, limit, offset);

        int total = inquiryRepository.countAllInquiryUserPage(memberId);

        List<MemberInquiryDTO> dtos = results.stream().map(obj -> new MemberInquiryDTO(
                (String) obj[0],  // inquiryNo
                (String) obj[1],  // memberId
                (String) obj[2],  // inquiryContent
                (Integer) obj[3], // showStatus
                (Integer) obj[4], // qnaType
                (String) obj[5],  // productNo
                ((Timestamp) obj[6]).toLocalDateTime(), // qnaWriteTime
                (String) obj[7],  // userName (from tbl_member)
                (String) obj[8],  // productName (from tbl_product)
                (String) obj[9],  // phoneNumber (from tbl_member)
                (String) obj[10],  // productImageLink (from tbl_product)
                (String) obj[11]
        )).collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO response = new PagingResponseDTO();
        response.setData(dtos);
        response.setPageInfo(pageInfo);

        return response;
    }

}
