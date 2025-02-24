package com.ohgiraffers.funniture.point.model.service;

import com.ohgiraffers.funniture.point.model.dao.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;


    public int findPointByUser(String memberId) {
        return pointRepository.findCurrentPointByUser(memberId);
    }
}
