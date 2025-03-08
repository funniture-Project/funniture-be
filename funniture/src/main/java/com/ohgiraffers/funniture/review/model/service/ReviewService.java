package com.ohgiraffers.funniture.review.model.service;

import com.ohgiraffers.funniture.inquiry.entity.InquiryEntity;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.review.entity.ReviewEntity;
import com.ohgiraffers.funniture.review.model.dao.ReviewRepository;
import com.ohgiraffers.funniture.review.model.dto.ReviewRegistDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

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
}
