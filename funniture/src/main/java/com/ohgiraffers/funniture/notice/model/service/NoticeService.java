package com.ohgiraffers.funniture.notice.model.service;

import com.ohgiraffers.funniture.notice.entity.NoticeEntity;
import com.ohgiraffers.funniture.notice.model.dao.NoticeRepository;
import com.ohgiraffers.funniture.notice.model.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final ModelMapper modelMapper;
    private final NoticeRepository noticeRepository;

    public List<NoticeDTO> getAllNoticeList() {
        List<NoticeEntity> noticeList =  noticeRepository.findAll();

        List<NoticeDTO> dtoList = noticeList.stream().map(notice -> modelMapper.map(notice, NoticeDTO.class))
                                                    .collect(Collectors.toList());

        return dtoList;
    }

    @Transactional
    public boolean registerNotice(NoticeDTO notice) {

        NoticeEntity newNotice = noticeRepository.save(modelMapper.map(notice, NoticeEntity.class));

        System.out.println("newNotice = " + newNotice);

        return newNotice != null ? true : false;
    }

    @Transactional
    public boolean deleteNotice(String noticeNo) {
        try{
            noticeRepository.deleteById(Integer.valueOf(noticeNo));
            return true;
        } catch (Exception e){
            System.out.println("삭제 중 오류 e = " + e);
            return false;
        }
    }
}
