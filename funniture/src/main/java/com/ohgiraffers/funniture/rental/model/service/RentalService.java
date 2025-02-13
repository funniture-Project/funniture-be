package com.ohgiraffers.funniture.rental.model.service;

import com.ohgiraffers.funniture.rental.entity.RentalEntity;
import com.ohgiraffers.funniture.rental.model.dao.*;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.RentalDTO;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalSearchCriteria;
import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalMapper rentalMapper;
    private final RentalRepository rentalRepository;
    private final ModelMapper modelMapper;
    private final AdminRentalRepositoryCustom adminRentalRepositoryCustom;
    private final UserRentalRepositoryCustom userRentalRepositoryCustom;

    // 사용자 - 예약 등록
    @Transactional
    public void insertRental(RentalDTO rentalDTO) {

        // 주문일 가져오기 (현재 시간)
        LocalDateTime orderDate = LocalDateTime.now();

        // 날짜만 추출하여 LocalDate 형식으로 변환
        LocalDate orderDateOnly = orderDate.toLocalDate();

        // 해당 날짜의 기존 예약 개수 조회
        int count = rentalRepository.countByOrderDate(orderDateOnly);

        // 새로운 예약번호 생성 (YYYYMMDD + 3자리 숫자)
        String rentalNo = orderDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%03d", count + 1);
        System.out.println("rentalNo = " + rentalNo);

        // DTO에 예약번호 & 주문일 설정 (Setter 사용)
        rentalDTO.setRentalNo(rentalNo);   // 예약번호 설정
        rentalDTO.setOrderDate(orderDate);  // 주문일 설정 (날짜 + 시간)
        System.out.println("orderDate = " + orderDate);

        // JPA 저장
        rentalRepository.save(modelMapper.map(rentalDTO, RentalEntity.class));
    }

    // 사용자 - 예약 조회(쿼리DSL)
    public List<UserOrderViewDTO> findRentalOrderListByUser(String memberId, String period, LocalDate searchDate) {
        return userRentalRepositoryCustom.findRentalOrderListByUser(memberId,period, searchDate);
    }


    // 관리자 - 예약 전체 조회(쿼리DSL)
    public List<AdminRentalViewDTO> findRentalAllListByAdmin(AdminRentalSearchCriteria criteria) {
        return adminRentalRepositoryCustom.findRentalAllListByAdmin(criteria);
    }



}
