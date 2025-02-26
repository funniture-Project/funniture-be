package com.ohgiraffers.funniture.favorite.controller;

import com.ohgiraffers.funniture.favorite.model.dto.FavoriteDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Favorite API")
@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // product 조회 (전체 상품 조회 및 categoryCode 별 조회, 제공자 별 상품 조회)
    @Operation(summary = "상품 조회 (상품 정보 + 가격 리스트)",
            description = "전체 상품 조회 및 categoryCode 별 조회, 제공자 별 상품 조회, 검색명으로 조회",
            parameters = {
                    @Parameter(name = "categoryCode", description = "조회할 카테고리 코드 리스트 (선택)"),
                    @Parameter(name = "ownerNo", description = "상품 제공자의 번호 리스트 (선택)"),
                    @Parameter(name = "searchText", description = "상품 검색명 (선택)"),
                    @Parameter(name = "productStatus", description = "판매 상태 (선택)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "등록된 상품이 없음"),
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping({"", "/"})
    public ResponseEntity<ResponseMessage> getProductAll(@RequestParam String memberId){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        List<FavoriteDTO> resultList = favoriteService.findListById(memberId);

        responseMap.put("result",resultList);

        System.out.println("resultList = " + resultList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "사용자 관심 상품 목록 조회 성공", responseMap));
    }


}
