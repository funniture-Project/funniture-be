package com.ohgiraffers.funniture.product.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @Tag : 관련 있는 API 들의 그룹을 짓기 위한 어노테이션
@Tag(name = "Product API")
@RestController
@RequestMapping("/product")
public class ProductController {
}
