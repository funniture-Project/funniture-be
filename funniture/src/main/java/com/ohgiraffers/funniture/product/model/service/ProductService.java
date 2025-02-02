package com.ohgiraffers.funniture.product.model.service;

import com.ohgiraffers.funniture.product.entity.CategoryEntity;
import com.ohgiraffers.funniture.product.entity.ProductEntity;
import com.ohgiraffers.funniture.product.model.dao.CategoryRepository;
import com.ohgiraffers.funniture.product.model.dao.ProductRepository;
import com.ohgiraffers.funniture.product.model.dto.CategoryDTO;
import com.ohgiraffers.funniture.product.model.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
// final 로 생성된 변수 자동 autowired
@RequiredArgsConstructor
public class ProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDTO> getProductAll(Integer categoryCode) {
        System.out.println("categoryCode = " + categoryCode);

        List<ProductEntity> productEntityList = new ArrayList<>();

        if (categoryCode == null ){
            productEntityList = productRepository.findAll();
        } else {
            productEntityList = productRepository.findByCategory_CategoryCode(categoryCode);
        }
        return productEntityList.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public ProductDTO getProductInfoByNo(String productNo) {

       ProductEntity product = productRepository.findById(productNo)
                                                .orElseThrow(IllegalArgumentException::new);

        // 값이 존재하면 DTO로 변환, 없으면 예외 발생 또는 기본 값 반환
        return modelMapper.map(product, ProductDTO.class);
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
}
