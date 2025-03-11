package com.ohgiraffers.funniture.comment.model.service;

import com.ohgiraffers.funniture.comment.entity.CommentEntity;
import com.ohgiraffers.funniture.comment.model.dao.CommentRepository;
import com.ohgiraffers.funniture.comment.model.dto.CommentByMyPageDTO;
import com.ohgiraffers.funniture.comment.model.dto.CommentByProductDTO;
import com.ohgiraffers.funniture.comment.model.dto.CommentRegistDTO;
import com.ohgiraffers.funniture.inquiry.entity.InquiryEntity;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    public int getMaxComment() {
        Integer maxCo = commentRepository.maxComment();
        System.out.println("maxCo 잘 받아 오는지 = " + maxCo);

        return maxCo == null ? 0 : maxCo;
    }

    public void commentRegist(CommentRegistDTO commentRegistDTO) {

        LocalDateTime currentTime = LocalDateTime.now();
        commentRegistDTO.setCommentWriteTime(currentTime);

        System.out.println("문의 답변 등록 세팅하고 결과 값 확인 : " + commentRegistDTO);

        commentRepository.save(modelMapper.map(commentRegistDTO , CommentEntity.class));
    }

    public CommentByMyPageDTO findByInquiryComment(String inquiryNo) {

        CommentEntity comment = commentRepository.findCommentByInquiryNo(inquiryNo);
        System.out.println("서비스에서 조회해 온 comment = " + comment);

        return modelMapper.map(comment , CommentByMyPageDTO.class);
    }

    // 상세 페이지에 답변 가져오기
    public CommentByProductDTO findCommentByProduct(String inquiryNo) {
        List<Object[]> result = commentRepository.findCommentByProductPage(inquiryNo);

        if (result == null || result.isEmpty()) {
            return null; // 결과가 없으면 null 반환
        }

        Object[] row = result.get(0); // 첫 번째 행 가져오기

        return new CommentByProductDTO(
                (Integer) row[0], // commentNo
                (String) row[1],  // memberId
                ((Timestamp) row[2]).toLocalDateTime(), // commentWriteTime
                (String) row[3],  // commentContent
                (Integer) row[4], // commentLevel
                row[5] != null ? (Integer) row[5] : null, // parentCommentNo (null 체크)
                (String) row[6],  // inquiryNo
                (String) row[7]   // storeName
        );
    }


//    public int getCommentLevel(Integer parentCommentNo) {
//        // DB에서 parentCommentNo에 해당하는 댓글의 level을 조회
//        Integer level = commentRepository.getCommentLevel(parentCommentNo);
//        return level == null ? 1 : level + 1; // level이 null이면 1로 설정
//    }
}
