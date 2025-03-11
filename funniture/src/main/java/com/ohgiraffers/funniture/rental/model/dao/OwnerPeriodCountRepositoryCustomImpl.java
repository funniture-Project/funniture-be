package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.QRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.RentalPeriodCountDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnerPeriodCountRepositoryCustomImpl implements OwnerPeriodCountRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    public List<RentalPeriodCountDTO> countRentalsByPeriod(String ownerNo, String period) {
        // 기간별 날짜 범위 설정
        LocalDate currentDate = LocalDate.now();
        LocalDateTime endDate = LocalDateTime.now(); // 기본적으로 현재 날짜로 설정
        LocalDateTime startDate = null;

        // 기간 버튼에 따라 날짜 범위 설정
        if ("1WEEK".equals(period)) {
            startDate = currentDate.minusWeeks(1).atStartOfDay();
        } else if ("1MONTH".equals(period)) {
            startDate = currentDate.minusMonths(1).atStartOfDay();
        } else if ("3MONTH".equals(period)) {
            startDate = currentDate.minusMonths(3).atStartOfDay();
        }


        // JPQL 쿼리 작성 (DISTINCT 추가)
        String jpql = "SELECT new com.ohgiraffers.funniture.rental.model.dto.RentalPeriodCountDTO(" +
                ":period, COUNT(r)) " + // DISTINCT 추가하여 중복 데이터 방지
                "FROM rental r " +
                "WHERE r.ownerNo = :ownerNo " +
                "AND r.rentalEndDate BETWEEN :startDate AND :endDate";

        return entityManager.createQuery(jpql, RentalPeriodCountDTO.class)
                .setParameter("period", period)
                .setParameter("ownerNo", ownerNo)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate) // 종료일 추가
                .getResultList();
    }
}