package com.ohgiraffers.funniture.product.controllers;

import com.ohgiraffers.funniture.product.model.dto.CategoryDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductDetailDTO;
import com.ohgiraffers.funniture.product.model.service.ProductService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Tag : 관련 있는 API 들의 그룹을 짓기 위한 어노테이션
@Tag(name = "Product API")
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ?categoryCode=10로 작성 시 해당 카테고리 상품만 출력, 없으면 전체
    // pathVariable 로 작성 시 {"/", "/{categoryCode}"}로 경로 작성해야 한다.

    // product 조회
    @GetMapping
    public ResponseEntity<ResponseMessage> getProductAll(@RequestParam(required = false) List<Integer> categoryCode){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        System.out.println("categoryCode = " + categoryCode);
        List<ProductDTO> result = productService.getProductAll(categoryCode);
        responseMap.put("result",result);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "전체 상품 리스트 조회 성공", responseMap));
    }
    
    // 상세 정보 조회
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

    @PostMapping("/register")
    public void registerProduct(@RequestBody ProductDTO product){

        String maxNo = productService.findMaxNO();

        System.out.println("mawNo = " + maxNo);

        if (maxNo == null || maxNo.isEmpty()){
            product.setProductNo("PRD001");
        } else {
            String newNo = String.format("PRD%03d", Integer.parseInt(maxNo.substring(3)) + 1);
            System.out.println("newNo = " + newNo);

            product.setProductNo(newNo);
        }

        System.out.println("product = " + product);

        productService.registerProduct(product);
    }

    @GetMapping("/category")
    private ResponseEntity<ResponseMessage> getCategoryList(@RequestParam(required = false) Integer refCategoryCode){

        List<CategoryDTO> categoryList = productService.getCategoryList(refCategoryCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType( "application","json",Charset.forName("UTF-8")));

        Map<String, Object> map = new HashMap<>();
        map.put("result",categoryList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "카테고리 전체 조회 성공", map));
    }

}
