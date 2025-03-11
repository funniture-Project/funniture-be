package com.ohgiraffers.funniture.comment.model.service;

import com.ohgiraffers.funniture.comment.entity.CommentEntity;
import com.ohgiraffers.funniture.comment.model.dao.CommentRepository;
import com.ohgiraffers.funniture.comment.model.dto.CommentByMyPageDTO;
import com.ohgiraffers.funniture.comment.model.dto.CommentRegistDTO;
import com.ohgiraffers.funniture.inquiry.entity.InquiryEntity;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

//    public int getCommentLevel(Integer parentCommentNo) {
//        // DB에서 parentCommentNo에 해당하는 댓글의 level을 조회
//        Integer level = commentRepository.getCommentLevel(parentCommentNo);
//        return level == null ? 1 : level + 1; // level이 null이면 1로 설정
//    }
}
