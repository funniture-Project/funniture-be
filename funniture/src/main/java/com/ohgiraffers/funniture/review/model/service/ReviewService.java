package com.ohgiraffers.funniture.review.model.service;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.PageDTO;
import com.ohgiraffers.funniture.common.PagingResponseDTO;
import com.ohgiraffers.funniture.inquiry.entity.InquiryEntity;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.funniture.inquiry.model.dto.MemberInquiryDTO;
import com.ohgiraffers.funniture.inquiry.model.dto.OwnerInquiryDTO;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.rental.model.dao.RentalRepository;
import com.ohgiraffers.funniture.review.entity.ReviewEntity;
import com.ohgiraffers.funniture.review.model.dao.ReviewRepository;
import com.ohgiraffers.funniture.review.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final RentalRepository rentalRepository;

    public String getMaxReview() {
        System.out.println("리뷰넘버 최대 찾아오는 애 ");
        String maxRe = reviewRepository.maxReview();
        System.out.println("maxRe 잘 받아 오는지 = " + maxRe);

        return maxRe;
    }

    @Transactional
    public void ReviewRegist(ReviewRegistDTO reviewRegistDTO) {
        LocalDateTime currentTime = LocalDateTime.now();
        reviewRegistDTO.setReviewWriteTime(currentTime);

        // memberId를 이용해 회원 정보 조회
        MemberEntity memberEntity = memberRepository.findById(reviewRegistDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));


        System.out.println("review , memberEntity에서 잘 찾아왔나 : " + memberEntity);

        reviewRepository.save(modelMapper.map(reviewRegistDTO , ReviewEntity.class));
    }


    //  작성 가능한 리뷰 항목들
    public PagingResponseDTO findWritableReviews(String memberId, Criteria cri) {
        System.out.println("작성 가능한 리뷰 데이터 요청: memberId=" + memberId + ", cri=" + cri);

        int offset = (cri.getPageNum() - 1) * cri.getAmount();
        int limit = cri.getAmount();

        List<Object[]> results = rentalRepository.findWritableReviews(memberId, limit, offset);
        int total = rentalRepository.countWritableReviews(memberId);

        List<WritableReviewDTO> dtos = results.stream().map(obj -> new WritableReviewDTO(
                (String) obj[0],  // rentalNo
                ((Timestamp) obj[1]).toLocalDateTime(),  // orderDate
                (String) obj[2],  // productNo
                (String) obj[3],  // productName
                (String) obj[4],  // productImageLink
                (Integer) obj[5], // rentalTerm
                (Integer) obj[6]  // rentalPrice
        )).collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO response = new PagingResponseDTO();
        response.setData(dtos);
        response.setPageInfo(pageInfo);

        return response;
    }


    // 작성한 리뷰 항목들
    public PagingResponseDTO findWrittenReviews(String memberId, Criteria cri) {
        System.out.println("작성한 리뷰 데이터 요청: memberId=" + memberId + ", cri=" + cri);

        int offset = (cri.getPageNum() - 1) * cri.getAmount();
        int limit = cri.getAmount();

        List<Object[]> results = reviewRepository.findWrittenReviews(memberId, limit, offset);
        int total = reviewRepository.countWrittenReviews(memberId);

        List<ReviewDTO> dtos = results.stream().map(obj -> new ReviewDTO(
                (String) obj[0],  // reviewNo
                ((Timestamp) obj[1]).toLocalDateTime(),  // reviewWriteTime
                (String) obj[2],  // reviewContent (작성된 내용)
                (String) obj[3],  // memberId
                (String) obj[4],  // productNo
                (Float) obj[5],   // score
                (String) obj[6],  // productName
                (String) obj[7],  // productImageLink
                (String) obj[8]   // rentalState
        )).collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO response = new PagingResponseDTO();
        response.setData(dtos);
        response.setPageInfo(pageInfo);

        return response;
    }



    public List<ReviewProductDTO> findReviewByProductNo(String productNo) {
        List<Object[]> results = reviewRepository.findDetailedReviewByProductNo(productNo);

        return results.stream().map(obj -> new ReviewProductDTO(
                (String) obj[0], // reviewNo
                ((Timestamp) obj[1]).toLocalDateTime(), // reviewWriteTime
                (String) obj[2], // reviewContent
                (String) obj[3], // memberId
                (String) obj[4], // productNo
                (float) obj[5], // score
                (String) obj[6], // productName
                (String) obj[7], // productImageLink
                (String) obj[8], // userName
                (int) obj[9] // rentalTerm
        )).collect(Collectors.toList());
    }

    public PagingResponseDTO findReviewsOfProductsByOwner(String ownerNo, Criteria cri) {
        int offset = (cri.getPageNum() - 1) * cri.getAmount();
        int limit = cri.getAmount();
        List<Object[]> results = reviewRepository.findReviewsOfProductsByOwner(ownerNo, limit, offset);

        int total = reviewRepository.countReviewsOfProductsByOwner(ownerNo);

        List<OwnerReviewDTO> dtos = results.stream().map(obj -> new OwnerReviewDTO(
                (String) obj[0], // reviewNo
                ((Timestamp) obj[1]).toLocalDateTime(), // reviewWriteTime
                (String) obj[2], // reviewContent
                (String) obj[3], // memberId
                (String) obj[4], // productNo
                (float) obj[5], // score
                (String) obj[6], // productName
                (String) obj[7], // productImageLink
                (String) obj[8], // userName
                (int) obj[9] // rentalTerm
        )).collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO response = new PagingResponseDTO();
        response.setData(dtos);
        response.setPageInfo(pageInfo);

        return response;
    }

    public List<ReviewMainDTO> findReviewByMain() {
        List<Object[]> results = reviewRepository.findAllReviewByMain();

        return results.stream().map(obj -> new ReviewMainDTO(
                (String) obj[0], // reviewNo
                ((Timestamp) obj[1]).toLocalDateTime(), // reviewWriteTime
                (String) obj[2], // reviewContent
                (String) obj[3], // memberId
                (String) obj[4], // productNo
                (float) obj[5], // score
                (String) obj[6], // productName
                (String) obj[7]  // userName
        )).collect(Collectors.toList());
    }

}
