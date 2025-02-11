package com.ohgiraffers.funniture.product.model.service;

import com.ohgiraffers.funniture.product.entity.CategoryEntity;
import com.ohgiraffers.funniture.product.entity.ProductDetailEntity;
import com.ohgiraffers.funniture.product.entity.ProductEntity;
import com.ohgiraffers.funniture.product.entity.ProductWithPriceEntity;
import com.ohgiraffers.funniture.product.model.dao.CategoryRepository;
import com.ohgiraffers.funniture.product.model.dao.ProductDetailRepository;
import com.ohgiraffers.funniture.product.model.dao.ProductRepository;
import com.ohgiraffers.funniture.product.model.dao.ProductWithPriceRepository;
import com.ohgiraffers.funniture.product.model.dto.CategoryDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductDetailDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductWithPriceDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
// final 로 생성된 변수 자동 autowired
@RequiredArgsConstructor
public class ProductService {

    private final ModelMapper modelMapper;
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final ProductWithPriceRepository ProductWithPriceRepository;
    private final CategoryRepository categoryRepository;

    // 전체 상품 조회, 카테고리별 상품 조회(상품 + 가격 리스트)
    public List<ProductWithPriceDTO> getProductAll(List<Integer> categoryCode) {

        List<ProductWithPriceEntity> productEntityList = new ArrayList<>();

        if (categoryCode == null ){
            productEntityList = ProductWithPriceRepository.findAllProductsList();
        } else {
            productEntityList = ProductWithPriceRepository.findAllProductsListByCategoryIn(categoryCode);
        }
        return productEntityList.stream().map(product -> modelMapper.map(product, ProductWithPriceDTO.class))
                .collect(Collectors.toList());
    }

    // 상품 번호에따른 상품 상세 조회
    public ProductDetailDTO getProductInfoByNo(String productNo) {

        ProductDetailEntity product = productDetailRepository.findById(productNo)
                .orElseThrow(IllegalArgumentException::new);

        // 값이 존재하면 DTO로 변환, 없으면 예외 발생 또는 기본 값 반환
        return modelMapper.map(product, ProductDetailDTO.class);
    }

    // 제공자별 상품 조회
    public List<ProductDetailDTO> getProductInfoByOwner(String ownerNo){

        List<ProductDetailEntity> productList = productDetailRepository.findAllByOwnerNo(ownerNo);

        return productList.stream().map(product -> modelMapper.map(product, ProductDetailDTO.class))
                .collect(Collectors.toList());
    }

    // 카테고리 조회
    public List<CategoryDTO> getCategoryList(Integer refCategoryCode) {

        List<CategoryEntity> categoryList = new ArrayList<>();

        if (refCategoryCode == null ){
            categoryList = categoryRepository.findAll();
        } else {
            // 상위 카테고리에 따른 카테고리
            categoryList = categoryRepository.findByRefCategoryCode(refCategoryCode);
        }

        return categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    // 상품 등록
    @Transactional
    public void registerProduct(ProductDTO product) {
        try {
            productRepository.save(modelMapper.map(product, ProductEntity.class));
            System.out.println("상품 등록 성공!!");
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
        }
    }

    // 상품 현재 번호 확인
    public String findMaxNO() {
        return productRepository.findMaxNo();
    }


    // 특이한 방법이라 추후 정리할 예정 그냥 둬주세요!!
//    public List<ProductWithPriceDTO> getAllProductsWithPrices() {
//        List<Object[]> results = productRepository.findAllProductsWithPriceList();
//        return results.stream()
//                .map(ProductWithPriceDTO::fromQueryResult)
//                .toList();
//    }

}
