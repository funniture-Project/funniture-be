package com.ohgiraffers.funniture.point.controllers;

import com.ohgiraffers.funniture.point.model.service.PointService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Tag(name = "point API")
@RestController
@RequestMapping("/api/v1/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMessage> findPointByUser(@PathVariable String memberId) {
        // 서비스에서 포인트 조회
        int availablePoints = pointService.findPointByUser(memberId);

        // 결과 반환
        if (availablePoints >= 0) {
            Map<String, Object> results = new HashMap<>();
            results.put("availablePoints", availablePoints);

            return ResponseEntity.ok(new ResponseMessage(200, "포인트 조회 성공", results));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage(404, "회원 정보를 찾을 수 없습니다.", null));
        }
    }
}

