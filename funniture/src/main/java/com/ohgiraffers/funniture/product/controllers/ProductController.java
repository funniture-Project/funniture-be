package com.ohgiraffers.funniture.product.controllers;

import com.ohgiraffers.funniture.product.model.dto.CategoryDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductDTO;
import com.ohgiraffers.funniture.product.model.service.ProductService;
import com.ohgiraffers.funniture.response.ResponseMessage;
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

// @Tag : 관련 있는 API 들의 그룹을 짓기 위한 어노테이션
@Tag(name = "Product API")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ?categoryCode=10로 작성 시 해당 카테고리 상품만 출력, 없으면 전체
    // pathVariable 로 작성 시 {"/", "/{categoryCode}"}로 경로 작성해야 한다.
    @GetMapping
    public ResponseEntity<ResponseMessage> getProductAll(@RequestParam(required = false) Integer categoryCode){
        System.out.println("카테고리 여부 출략");
        System.out.println("categoryCode = " + categoryCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        List<ProductDTO> result = productService.getProductAll(categoryCode);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result",result);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "전체 상품 리스트 조회 성공", responseMap));
    }

    @GetMapping("/category")
    private ResponseEntity<ResponseMessage> getCategoryList(){

        List<CategoryDTO> categoryList = productService.getCategoryList();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType( "application","json",Charset.forName("UTF-8")));

        Map<String, Object> map = new HashMap<>();
        map.put("result",categoryList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "카테고리 전체 조회 성공", map));
    }

}
