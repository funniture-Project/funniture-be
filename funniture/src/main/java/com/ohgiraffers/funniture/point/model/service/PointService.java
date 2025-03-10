package com.ohgiraffers.funniture.point.model.service;

import com.ohgiraffers.funniture.point.entity.PointEntity;
import com.ohgiraffers.funniture.point.model.dao.PointRepository;
import com.ohgiraffers.funniture.point.model.dto.PointDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final ModelMapper modelMapper;


    public int findPointByUser(String memberId) {
        return pointRepository.findCurrentPointByUser(memberId);
    }

    public List<PointDTO> getPointLogsByUser(String memberId) {
        List<PointEntity> pointLogs = pointRepository.findByMemberIdOrderByPointDateTimeDesc(memberId);

        return pointLogs.stream()
                .map(pointLog -> modelMapper.map(pointLog, PointDTO.class))
                .collect(Collectors.toList());
    }


}
