package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.QAdminCategoryEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminOwnerInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminProductEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalSearchCriteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRentalRepositoryCustomImpl implements AdminRentalRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AdminRentalViewDTO> findRentalAllListByAdmin(AdminRentalSearchCriteria criteria) {

        QAdminRentalEntity rental = QAdminRentalEntity.adminRentalEntity;
        QAdminProductEntity product = QAdminProductEntity.adminProductEntity;
        QAdminOwnerInfoEntity ownerInfo = QAdminOwnerInfoEntity.adminOwnerInfoEntity;
        QAdminCategoryEntity category = QAdminCategoryEntity.adminCategoryEntity;
        QAdminCategoryEntity parentCategory = new QAdminCategoryEntity("parentCategory");

        BooleanBuilder builder = new BooleanBuilder();

        // 조건 설정
        // 1. 진행상태
        if(criteria.getRentalState() != null){
            builder.and(rental.rentalState.eq(criteria.getRentalState()));
        }
        // 2. 회사명
        if(criteria.getStoreName() != null){
            builder.and(ownerInfo.storeName.eq(criteria.getStoreName()));
        }
        // 3. 상위카테고리이름(가전/가구)
        if(criteria.getCategoryName() != null){
            builder.and(parentCategory.categoryName.eq(criteria.getCategoryName()));
        }
        // 4. 검색날짜(사용날짜이상 만료날짜 이하 사이의 날짜면 다 조회)
        // loe '<=' goe '>='
        if (criteria.getSearchDate() != null) {
            LocalDateTime searchDateTime = criteria.getSearchDate(); // LocalDateTime
            LocalDate searchDate = searchDateTime.toLocalDate(); // LocalDate로 변환

            LocalDateTime startOfDay = searchDate.atStartOfDay(); // ✅ 2025-02-01 00:00:00
            LocalDateTime endOfDay = searchDate.atTime(23, 59, 59); // ✅ 2025-02-01 23:59:59

            builder.and(rental.rentalStartDate.loe(endOfDay)) // rentalStartDate <= 2025-02-01 23:59:59
                    .and(rental.rentalEndDate.goe(startOfDay)); // rentalEndDate >= 2025-02-01 00:00:00
        }

        if (criteria.getRentalNo() != null) {
            builder.and(rental.rentalNo.like("%" + criteria.getRentalNo() + "%"));
        }

        // 쿼리실행
        return jpaQueryFactory
                .select(Projections.constructor(AdminRentalViewDTO.class,
                        rental.rentalNo,
                        ownerInfo.storeName,
                        rental.rentalState,
                        parentCategory.categoryName,
                        product.productName,
                        rental.rentalStartDate,
                        rental.rentalEndDate,
                        rental.rentalNumber))
                .from(rental)
                .join(rental.adminProduct, product)
                .join(product.adminOwnerInfo, ownerInfo)
                .join(product.adminCategory, category)
                .leftJoin(category.refCategoryCode, parentCategory)
                .where(builder)
                .fetch();
    }
}
