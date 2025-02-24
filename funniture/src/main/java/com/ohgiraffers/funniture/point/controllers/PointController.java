package com.ohgiraffers.funniture.point.controllers;

import com.ohgiraffers.funniture.point.model.dto.PointDTO;
import com.ohgiraffers.funniture.point.model.service.PointService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Tag(name = "point API")
@RestController
@RequestMapping("/api/v1/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @Operation(summary = "사용자별 보유포인트 조회",
            description = "사용자 마이페이지, 예약등록페이지에서 사용",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID(필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "404",description = "회원 정보를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공")
    })
    // 사용자별 보유 포인트 조회
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

    @Operation(summary = "사용자별 포인트내역 조회",
            description = "사용자 마이페이지에서 사용",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID(필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "포인트 내역이 없습니다."),
            @ApiResponse(responseCode = "200", description = "포인트 내역 조회 완료")
    })
    // 사용자별 포인트내역(충전포인트, 사용포인트 내역)
    @GetMapping("logs/{memberId}")
    public ResponseEntity<ResponseMessage> getPointLogs(@PathVariable String memberId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<PointDTO> pointLogs = pointService.getPointLogsByUser(memberId);

        if (pointLogs.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "포인트 내역이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("pointLogs", pointLogs);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "포인트 내역 조회 완료", res));
    }

}

