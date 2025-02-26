package com.ohgiraffers.funniture.product.model.service;

import com.ohgiraffers.funniture.common.ProductSearchCondition;
import com.ohgiraffers.funniture.product.entity.*;
import com.ohgiraffers.funniture.product.model.dao.*;
import com.ohgiraffers.funniture.product.model.dto.*;
import lombok.RequiredArgsConstructor;
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
    private final RentalOptionInfoRepository rentalOptionInfoRepository;
    private final ProductWithPriceRepository productWithPriceRepository;

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
            ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
            System.out.println("변환된 productEntity = " + productEntity);
            productRepository.save(productEntity);
//            productRepository.save(modelMapper.map(product, ProductEntity.class));
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

    @Transactional
    public Integer saveOptionList(List<RentalOptionInfoDTO> rentalOptionList) {

        List<RentalOptionInfoEntity> optionInfoEntityList = rentalOptionList.stream()
                .map(dto -> modelMapper.map(dto, RentalOptionInfoEntity.class))
                .collect(Collectors.toList());

        List<RentalOptionInfoEntity> savedEntities = rentalOptionInfoRepository.saveAll(optionInfoEntityList);

        if (savedEntities.isEmpty()) {
            System.out.println( "저장에 실패했습니다.");

            return null;
        }

        return savedEntities.size();
    }

    @Transactional
    public Integer updateProductInfo(String productNo, ProductDTO product) {
        try{
            ProductEntity findProduct = productRepository.findById(productNo)
                    .orElse(null);

            if (findProduct != null){
                findProduct.update(modelMapper.map(product,ProductEntity.class));

                return 204;
            } else {
                return 404;
            }
        } catch (Exception e){
            return 500;
        }
    }

    @Transactional
    public Integer updateRentalOption(String productNo, List<RentalOptionInfoDTO> rentalOptionList) {

        try{
            System.out.println("새로 저장될 rentalOptionList = " + rentalOptionList);

            List<RentalOptionInfoEntity> rentalOptionInfoEntityList = rentalOptionInfoRepository.findAllByProductNo(productNo);
            System.out.println("변경 전 rentalOptionInfoEntityList = " + rentalOptionInfoEntityList);

            for (RentalOptionInfoEntity exist : rentalOptionInfoEntityList){
                boolean change = false;

                for (RentalOptionInfoDTO newOption : rentalOptionList){
                    // 원래 있던걸 변경한 경우
                    if (exist.getRentalInfoNo() == newOption.getRentalInfoNo()){
                        exist.update(newOption);
                        rentalOptionInfoRepository.save(exist);
                        change = true;
                        break;
                        // 새로 만들어서 저장한 경우
                    }

                    // 새로운 리스트와 비교했는데 여전히 false => 사용하지 않는 옵션이 됐다는 뜻
                    if (!change){
                        exist = exist.toBuilder().isActive(false).build();
                        rentalOptionInfoRepository.save(exist);
                    }
                }
            }

            // 새로운 데이터 추가
            for (RentalOptionInfoDTO newOption : rentalOptionList) {
                if (newOption.getRentalInfoNo() == 0) { // 새로운 데이터만 처리
                    System.out.println("저장 시도 중: " + newOption);
                    RentalOptionInfoEntity newEntity = modelMapper.map(newOption, RentalOptionInfoEntity.class);
                    rentalOptionInfoRepository.save(newEntity);
                }
            }

            return 204;
        } catch (Exception e) {
            return 500;
        }
    }

    public List<RecentProductDTO> findAllProductInfo(List<String> productList) {
        List<RecentProductDTO> infoList = productWithPriceRepository.findAllProductInfo(productList);

        infoList.sort(Comparator.comparing(item -> productList.indexOf(item.getProductNo())));

        return infoList;
    }

    // 특이한 방법이라 추후 정리할 예정 그냥 둬주세요!!
//    public List<ProductWithPriceDTO> getAllProductsWithPrices() {
//        List<Object[]> results = productRepository.findAllProductsWithPriceList();
//        return results.stream()
//                .map(ProductWithPriceDTO::fromQueryResult)
//                .toList();
//    }
}
