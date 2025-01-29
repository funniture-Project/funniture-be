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

import java.util.List;
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

        if (categoryCode == null ){
            List<ProductEntity> productEntityList = productRepository.findAll();
    //        System.out.println("엔티티 조회 productEntityList = " + productEntityList);
            return productEntityList.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
        } else {
            List<ProductEntity> productEntityList = productRepository.findByCategory_CategoryCode(categoryCode);
            //        System.out.println("엔티티 조회 productEntityList = " + productEntityList);
            return productEntityList.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                    .collect(Collectors.toList());
        }
    }

    public List<CategoryDTO> getCategoryList() {

        List<CategoryEntity> categoryList = categoryRepository.findAll();

        return categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }
}
