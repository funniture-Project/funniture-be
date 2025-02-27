package com.ohgiraffers.funniture.favorite.controller;

import com.ohgiraffers.funniture.favorite.model.dto.FavoriteDetailInfoDTO;
import com.ohgiraffers.funniture.favorite.model.dto.FavoriteListDTO;
import com.ohgiraffers.funniture.favorite.model.service.FavoriteService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Favorite API")
@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 관심 상품 리스트 조회
    @Operation(summary = "관심 상품 리스트 조회",
            description = "사용자별 관심 상품 목록 조회",
            parameters = {
                    @Parameter(name = "memberId", description = "조회하는 사람의 ID")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "관심 상품 목록 없음"),
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping({"","/"})
    public ResponseEntity<ResponseMessage> getFavoriteList(@RequestParam String memberId){

        List<FavoriteListDTO> favoriteList = favoriteService.getFavoriteList(memberId);
        
        System.out.println("favoriteList = " + favoriteList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        if (favoriteList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "관심 상품 목록이 없습니다.", null));
        }

        responseMap.put("result",favoriteList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "사용자 관심 상품 목록 조회 성공", responseMap));
    }

    // 관심 상품 제품 정보 조회
    @Operation(summary = "관심 상품 조회 (상품 정보 + 가격 리스트)",
            description = "사용자별 관심 상품 페이지에 필요한 상세 정보 조회",
            parameters = {
                    @Parameter(name = "memberId", description = "조회하는 사람의 ID")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "404",description = "상세 정보를 찾지 못했습니다."),
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping(value = "/detailInfo")
    public ResponseEntity<ResponseMessage> getProductAll(@RequestParam String memberId){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        List<FavoriteDetailInfoDTO> resultList = favoriteService.findListById(memberId);
        
        if (resultList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(404, "상세 정보를 찾지 못했습니다.", null));
        }

        responseMap.put("result",resultList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "사용자 관심 상품 목록 조회 성공", responseMap));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<ResponseMessage> updateFavorite(@RequestBody Map<String, Object> request){
        System.out.println("request = " + request);

        String memberId = (String) request.get("memberId");
        List<String> favoriteList = (List<String>) request.get("favoriteList");

        System.out.println("memberId = " + memberId);
        System.out.println("favoriteList = " + favoriteList);

        // 기존 데이터 조회
        List<String> existList = favoriteService.getFavoriteList(memberId)
                                .stream().map(item -> item.getProductNo()).collect(Collectors.toList());

        System.out.println("existList = " + existList);

        // 삭제 될 데이터 찾기
        List<String> deleteData = existList.stream().filter(item -> !favoriteList.contains(item))
                .collect(Collectors.toList());

        boolean result = true;

        if (!deleteData.isEmpty()){
            result = favoriteService.deleteData(memberId, deleteData);
        }

        // 추가할 데이터
        List<String> addData = favoriteList.stream().filter(item -> !existList.contains(item))
                .collect(Collectors.toList());

        List<Map<String, String>> addFavoriteList = new ArrayList<>();

        if (!addData.isEmpty()){
            for (String productNo : addData){
                Map<String, String> favoriteItem = new HashMap<>();
                favoriteItem.put("productNo", productNo);
                favoriteItem.put("memberId", memberId);
                addFavoriteList.add(favoriteItem);
            }

            result = favoriteService.addData(addFavoriteList);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (result){

            Map<String, Object> responseMap = new HashMap<>();

            List<FavoriteListDTO> resultList = favoriteService.getFavoriteList(memberId);

            responseMap.put("result",resultList);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(200, "업데이트 성공", responseMap));

        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(500, "업데이트 과정에서 문제가 생겼습니다.", null));
    }


}
