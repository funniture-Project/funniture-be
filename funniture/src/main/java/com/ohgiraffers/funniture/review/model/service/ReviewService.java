package com.ohgiraffers.funniture.review.model.service;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.PageDTO;
import com.ohgiraffers.funniture.common.PagingResponseDTO;
import com.ohgiraffers.funniture.inquiry.entity.InquiryEntity;
import com.ohgiraffers.funniture.inquiry.model.dto.MemberInquiryDTO;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.review.entity.ReviewEntity;
import com.ohgiraffers.funniture.review.model.dao.ReviewRepository;
import com.ohgiraffers.funniture.review.model.dto.ReviewDTO;
import com.ohgiraffers.funniture.review.model.dto.ReviewRegistDTO;
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

    public PagingResponseDTO findByReviewUserPage(String memberId, Criteria cri) {
        System.out.println("컨트롤러에서 데이터 잘 넘어 왔는지? : " + memberId + "  " + cri);

        int offset = (cri.getPageNum() - 1) * cri.getAmount();
        int limit = cri.getAmount();

        List<Object[]> results = reviewRepository.findAllReviewUserPage(memberId, limit, offset);

        int total = reviewRepository.countAllReviewUserPage(memberId);

        List<ReviewDTO> dtos = results.stream().map(obj -> new ReviewDTO(
                (String) obj[0],  // reviewNo
                ((Timestamp) obj[1]).toLocalDateTime(),  // reviewWriteTime
                (String) obj[2],  // reviewContent
                (String) obj[3],  // memberId
                (String) obj[4],  // productNo
                (Float) obj[5],   // score
                (String) obj[6],   // productName
                (String) obj[7]   // productImageLink
        )).collect(Collectors.toList());

        System.out.println("리뷰 조회 매핑 잘 됐는지?");
        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO response = new PagingResponseDTO();
        response.setData(dtos);
        response.setPageInfo(pageInfo);

        return response;
    }


}
