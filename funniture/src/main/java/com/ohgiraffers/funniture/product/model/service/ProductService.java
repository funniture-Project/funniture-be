package com.ohgiraffers.funniture.product.model.service;

import com.ohgiraffers.funniture.product.entity.CategoryEntity;
import com.ohgiraffers.funniture.product.entity.ProductDetailEntity;
import com.ohgiraffers.funniture.product.entity.ProductEntity;
import com.ohgiraffers.funniture.product.model.dao.CategoryRepository;
import com.ohgiraffers.funniture.product.model.dao.ProductDetailRepository;
import com.ohgiraffers.funniture.product.model.dao.ProductRepository;
import com.ohgiraffers.funniture.product.model.dto.CategoryDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductDetailDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductWithPriceDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
// final 로 생성된 변수 자동 autowired
@RequiredArgsConstructor
public class ProductService {

    private final ModelMapper modelMapper;
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // 상품 조회
    public List<ProductDTO> getProductAll(List<Integer> categoryCode) {
        System.out.println("categoryCode = " + categoryCode);

        List<ProductEntity> productEntityList = new ArrayList<>();

        if (categoryCode == null ){
            productEntityList = productRepository.findAll();
        } else {
            productEntityList = productRepository.findByCategoryCodeIn(categoryCode);
        }
        return productEntityList.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public ProductDetailDTO getProductInfoByNo(String productNo) {

       ProductDetailEntity product = productDetailRepository.findById(productNo)
                                                .orElseThrow(IllegalArgumentException::new);

        // 값이 존재하면 DTO로 변환, 없으면 예외 발생 또는 기본 값 반환
        return modelMapper.map(product, ProductDetailDTO.class);
    }

    public List<ProductWithPriceDTO> getAllProductsWithPrices() {
        List<Object[]> results = productRepository.findAllProductsWithPriceList();
        return results.stream()
                .map(ProductWithPriceDTO::fromQueryResult)
                .toList();
    }

    public List<CategoryDTO> getCategoryList(Integer refCategoryCode) {

        List<CategoryEntity> categoryList = new ArrayList<>();

        if (refCategoryCode == null ){
            categoryList = categoryRepository.findAll();
        } else {
            categoryList = categoryRepository.findByRefCategoryCode(refCategoryCode);
        }

        return categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void registerProduct(ProductDTO product) {
        try {
            productRepository.save(modelMapper.map(product, ProductEntity.class));
            System.out.println("상품 등록 성공!!");
        } catch (Exception e) {
            System.out.println("error = " + e.getMessage());
        }
    }

    public String findMaxNO() {
        return productRepository.findMaxNo();
    }
}
