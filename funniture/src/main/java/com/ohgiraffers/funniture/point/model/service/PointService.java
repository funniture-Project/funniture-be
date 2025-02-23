package com.ohgiraffers.funniture.point.model.service;

import com.ohgiraffers.funniture.point.entity.PointEntity;
import com.ohgiraffers.funniture.point.model.dao.PointHistoryRepository;
import com.ohgiraffers.funniture.point.model.dao.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public int findPointByUser(String memberId) {
        // 포인트 정보 조회 (초기 포인트 + 충전 포인트)
        Optional<PointEntity> pointEntity = pointRepository.findById(memberId);

        if (pointEntity.isPresent()) {
            // 포인트 사용 내역 합산
            int usedPoints = pointHistoryRepository.sumUsedPointsByMemberId(memberId);

            // 남은 포인트 계산
            int availablePoints = pointEntity.get().getInitialPoint() + pointEntity.get().getAddPoint() - usedPoints;
            return availablePoints;
        } else {
            // 회원이 존재하지 않으면 -1 반환
            return -1;
        }

    }
}
