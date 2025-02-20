package com.ohgiraffers.funniture.product.model.service;

import com.ohgiraffers.funniture.common.ProductSearchCondition;
import com.ohgiraffers.funniture.product.entity.CategoryEntity;
import com.ohgiraffers.funniture.product.entity.ProductDetailEntity;
import com.ohgiraffers.funniture.product.entity.ProductEntity;
import com.ohgiraffers.funniture.product.entity.ProductWithPriceEntity;
import com.ohgiraffers.funniture.product.model.dao.*;
import com.ohgiraffers.funniture.product.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public List<ProductWithPriceDTO> getProductAll(ProductSearchCondition condition) {

        List<ProductWithPriceEntity> productEntityList = new ArrayList<>();

        productEntityList = ProductWithPriceRepository.findSearchProductList(condition);

        return productEntityList.stream().map(product -> modelMapper.map(product, ProductWithPriceDTO.class))
                .collect(Collectors.toList());
    }

    // 상품 번호에따른 상품 상세 조회
    public ProductDetailDTO getProductInfoByNo(String productNo) {

        Optional<ProductDetailEntity> product = productDetailRepository.findById(productNo);

        // 값이 존재하면 DTO로 변환, 없으면 예외 발생 또는 기본 값 반환
        return modelMapper.map(product, ProductDetailDTO.class);
    }

    // 제공자별 상품 조회
    public List<ProductDetailDTO> getProductInfoByOwner(String ownerNo){

        List<ProductDetailEntity> productList = productDetailRepository.findAllByOwnerInfo_memberId(ownerNo);

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
        } catch (Exception e) {
            System.out.println("e = " + e);
            System.out.println("error = " + e.getMessage());
        }

        System.out.println("상품 등록 성공!!");
    }

    // 상품 현재 번호 확인
    public String findMaxNO() {
        return productRepository.findMaxNo();
    }

    // 카테고리별 제공자 정보 확인
    public List<Map<String, String>> getOwnerByCategory(List<Integer> categoryCode) {

        System.out.println("categoryCode = " + categoryCode);

        List<Object[]> ownerList = new ArrayList<>();

        if (categoryCode.contains(1) || categoryCode.contains(2)){
            ownerList = productRepository.getOwnerByRefCategory(categoryCode);
        } else {
            ownerList = productRepository.getOwnerByCategory(categoryCode);
        }

        List<Map<String, String>> result = new ArrayList<>();

        for (Object[] owner : ownerList){
            Map<String, String> ownerInfo = new HashMap<>();

            ownerInfo.put("store_name", (String) owner[0]);
            ownerInfo.put("owner_no", (String) owner[1]);
            result.add(ownerInfo);
        }

        System.out.println("result = " + result);

        return result;
    }

    @Transactional
    public Map<Integer, String> modifyProductStatus(ChangeStatusDTO changeStatusList) {

        Map<Integer, String> response = new HashMap<>();

        if (!changeStatusList.getProductList().isEmpty() && changeStatusList.getChangeStatus() != null && changeStatusList.getChangeStatus().trim() != ""){

            for (String product : changeStatusList.getProductList()){
                ProductEntity foundProduct = productRepository.findById(product).orElse(null);

                if (foundProduct == null ){
                    response.put(404,"찾을 수 없는 상품의 정보가 포함되어 있습니다.");
                    return response;
                }

                foundProduct = foundProduct.toBuilder().productStatus(changeStatusList.getChangeStatus()).build();

                productRepository.save(foundProduct);
            }

            response.put(204,"제품 상태 변경 완료");
        } else {
            response.put(400,"잘못된 요청입니다.");
        }
        return response;
    }

    @Transactional
    public void deleteProduct(List<String> productList) {
        System.out.println("productList = " + productList);

    }


    // 특이한 방법이라 추후 정리할 예정 그냥 둬주세요!!
//    public List<ProductWithPriceDTO> getAllProductsWithPrices() {
//        List<Object[]> results = productRepository.findAllProductsWithPriceList();
//        return results.stream()
//                .map(ProductWithPriceDTO::fromQueryResult)
//                .toList();
//    }
}
