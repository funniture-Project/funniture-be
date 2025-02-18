package com.ohgiraffers.funniture.product.controllers;

import com.ohgiraffers.funniture.common.ProductSearchCondition;
import com.ohgiraffers.funniture.member.model.service.CustomUserDetailsService;
import com.ohgiraffers.funniture.product.model.dto.*;
import com.ohgiraffers.funniture.product.model.service.ProductService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Tag : 관련 있는 API 들의 그룹을 짓기 위한 어노테이션
//@CrossOrigin(origins = "http://localhost:3000") // 프론트나 백단 중 프록시 설정 1개만 있으면 된다.
@Tag(name = "Product API")
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CustomUserDetailsService customUserDetailsService;

    // ?categoryCode=10로 작성 시 해당 카테고리 상품만 출력, 없으면 전체
    // pathVariable 로 작성 시 {"/", "/{categoryCode}"}로 경로 작성해야 한다.

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
    public ResponseEntity<ResponseMessage> getProductAll(@ModelAttribute ProductSearchCondition condition){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        List<ProductWithPriceDTO> results = productService.getProductAll(condition);

        if (results.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록된 상품이 없습니다.", null));
        }

        responseMap.put("result",results);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "전체 상품 리스트 조회 성공", responseMap));
    }

    // 제공자별 등록한 상품 조회
    @Operation(summary = "제공자가 등록한 상품 조회 (상품 정보 + 렌탈 옵션 정보 + 카테고리 정보)",
            description = "제공자가 등록한 상품 조회",
            parameters = {
                    @Parameter(name = "ownerNo", description = "상품 제공자의 회원번호 (필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "등록된 상품이 없음"),
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/owner")
    public ResponseEntity<ResponseMessage> getProductAll(@RequestParam String ownerNo ){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        List<ProductDetailDTO> results = productService.getProductInfoByOwner(ownerNo);
        if (results.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록된 상품이 없습니다.", null));
        }

        responseMap.put("result",results);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "제공자가 등롣한 상품 리스트 조회 성공", responseMap));
    }

    // 상세 정보 조회 (상품 상세 페이지, 제공자 상품 수정)
    @Operation(summary = "상품 상세 정보 조회",
            description = "상품 상세 페이지, 제공자 상품 수정에서 사용",
            parameters = {
                    @Parameter(name = "productNo", description = "상품 번호 (필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "404",description = "상품을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "400",description = "잘못된 요청입니다. productNo를 확인해주세요"),
            @ApiResponse(responseCode = "200", description = "특정 상품 정보 조회 성공")
    })
    @GetMapping("/{productNo}")
    public ResponseEntity<ResponseMessage> getProductDetail(@PathVariable String productNo){
        Map<String, Object> responseMap = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (productNo != null){

            ProductDetailDTO result = productService.getProductInfoByNo(productNo);

            // 결과가 없을 경우 처리 (상품을 찾지 못한 경우)
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .headers(headers)
                        .body(new ResponseMessage(404, "상품을 찾을 수 없습니다", null));
            }

            responseMap.put("result",result);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(200, "특정 상품 정보 조회 성공", responseMap));
        } else {
            return ResponseEntity.badRequest()
                    .headers(headers)
                    .body(new ResponseMessage(400, "잘못된 요청입니다", null));
        }
    }

    // 상품 등록
    @Operation(summary = "상품 등록",
            description = "상품 등록 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "401",description = "필수값 부족. errors 확인"),
            @ApiResponse(responseCode = "400",description = "상품 등록에 실패."),
            @ApiResponse(responseCode = "201", description = "상품 등록 성공")
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerProduct(@Valid @RequestBody ProductDTO product, BindingResult bindingResult){

        Map<String, Object> responseMap = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (bindingResult.hasErrors()){
            System.out.println("bindingResult = " + bindingResult);

            responseMap.put("errors",bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(401, "필수값 부족",responseMap));
        } else {
            String maxNo = productService.findMaxNO();

            System.out.println("mawNo = " + maxNo);

            if (maxNo == null || maxNo.isEmpty()){
                product.setProductNo("PRD001");
            } else {
                String newNo = String.format("PRD%03d", Integer.parseInt(maxNo.substring(3)) + 1);
                System.out.println("newNo = " + newNo);

                product.setProductNo(newNo);
            }

            productService.registerProduct(product);

            String checkNo = productService.findMaxNO();
            System.out.println("product = " + product.getProductNo() + ", checkNo : " + checkNo);

            if (product.getProductNo().equals(checkNo)){

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(new ResponseMessage(201, "상품 등록 성공", null));
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(400, "잘못된 요청", null));
        }
    }

    // 상품 카테고리 조회, 상위 카테고리별 조회
    @Operation(summary = "카테고리 조회",
            description = "상품 카테고리 조회, 상위 카테고리별 조회 "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 전체 조회 성공"),
            @ApiResponse(responseCode = "204", description = "카테고리 없음")
    })
    @GetMapping("/category")
    private ResponseEntity<ResponseMessage> getCategoryList(@RequestParam(required = false) Integer refCategoryCode){
        List<CategoryDTO> categoryList = productService.getCategoryList(refCategoryCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType( "application","json",Charset.forName("UTF-8")));

        Map<String, Object> map = new HashMap<>();

        if (categoryList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록된 카테고리 없음", null));
        }

        map.put("result",categoryList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "카테고리 전체 조회 성공", map));
    }

    // 카테고리별 제공자 리스트 조회
    @Operation(summary = "카테고리별 제공자 리스트 조회",
            description = "상위 카테고리별 및 카테고리별 해당 제품 등록한 제공자 리스트 조회 "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리에 따른 제공자 정보 조회 성공"),
            @ApiResponse(responseCode = "204", description = "카테고리에 따른 제공자 정보 조회 없음")
    })
    @GetMapping(value = "/ownerlist")
    private ResponseEntity<ResponseMessage> getOwnerByCategory(@RequestParam(required = false) List<Integer> categoryCode){

        List<Map<String, String>> result = new ArrayList<>();

        if (categoryCode != null){
            result = productService.getOwnerByCategory(categoryCode);
        }else {
            result = customUserDetailsService.findAllOwner();
        }

        Map<String, Object> map = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType( "application","json",Charset.forName("UTF-8")));

        map.put("result",result);

        if (result.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "카테고리에 따른 제공자 정보 조회 없음", null));
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "제공자 정보 조회 성공", map));
    }


    // 상품 상태 변경
    @Operation(summary = "상품 상태 변경",
            description = "관리자 페이지, 제공자 페이지에서 사용하는 상품별 상태 변경"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "제품 상태 변경 완료"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 상품의 정보가 포함되어 있습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PutMapping(value = "/changestatus")
    private ResponseEntity<ResponseMessage> modifyProductStatus(@RequestBody ChangeStatusDTO changeStatusList ){
        System.out.println("changeStatusList = " + changeStatusList);

        Map<Integer, String> result = productService.modifyProductStatus(changeStatusList);

        Integer code = result.keySet().stream().findFirst().orElse(500);
        String msg = "내부적인 오류 발생";
        if (code != null){
            msg = result.get(code);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(code, msg, null));
    }

}
