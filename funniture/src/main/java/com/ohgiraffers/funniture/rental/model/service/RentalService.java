package com.ohgiraffers.funniture.rental.model.service;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.deliveryaddress.entity.DeliveryAddressEntity;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.point.entity.PointEntity;
import com.ohgiraffers.funniture.point.model.dao.PointRepository;
import com.ohgiraffers.funniture.product.entity.ProductEntity;
import com.ohgiraffers.funniture.product.entity.RentalOptionInfoEntity;
import com.ohgiraffers.funniture.product.model.dao.ProductRepository;
import com.ohgiraffers.funniture.product.model.dao.RentalOptionInfoRepository;
import com.ohgiraffers.funniture.rental.entity.RentalEntity;
import com.ohgiraffers.funniture.rental.model.dao.*;
import com.ohgiraffers.funniture.rental.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final AdminSalesRepositoryCustom adminSalesRepositoryCustom;
    private final UserRentalRepositoryCustom userRentalRepositoryCustom;
    private final OwnerRentalRepositoryCustom ownerRentalRepositoryCustom;
    private final OwnerSalesRepositoryCustom ownerSalesRepositoryCustom;
    private final DetailRentalRepositoryCustom detailRentalRepositoryCustom;
    private final UserActiveRentalRepositoryCustom userActiveRentalRepositoryCustom;
    private final UserRentalStateCountRepositoryCustom userRentalStateCountRepositoryCustom;
    private final PointRepository pointRepository;
    private final RentalOptionInfoRepository rentalOptionInfoRepository;
    private final ProductRepository productRepository;


/* comment.-------------------------------------------- 사용자 -----------------------------------------------*/
    // 사용자 - 예약 등록
    @Transactional
    public void insertRental(RentalDTO rentalDTO) {

        // 주문일 가져오기 (현재 시간)
        LocalDateTime orderDate = LocalDateTime.now();
        LocalDate orderDateOnly = orderDate.toLocalDate();

        // 해당 날짜의 기존 예약 개수 조회
        int count = rentalRepository.countByOrderDate(orderDateOnly);

        // 새로운 예약번호 생성 (YYYYMMDD + 3자리 숫자)
        String rentalNo = orderDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + String.format("%03d", count + 1);

        String status = "예약대기";

        // DTO에 예약번호 & 주문일 설정 (Setter 사용)
        rentalDTO.setRentalNo(rentalNo);   // 예약번호 설정
        rentalDTO.setOrderDate(orderDate);  // 주문일 설정 (날짜 + 시간)
        rentalDTO.setRentalState(status);   // 예약진행상태 예약대기로

        // 최신 포인트 가져오기
        int currentPoints = pointRepository.findCurrentPointByUser(rentalDTO.getMemberId());

        // 렌탈 가격 가져오기
        RentalOptionInfoEntity rentalOption = rentalOptionInfoRepository.findById(rentalDTO.getRentalInfoNo())
                .orElseThrow(() -> new RuntimeException("대여 조건 정보 없음"));

        int rentalPrice = rentalDTO.getRentalNumber() * rentalOption.getRentalPrice(); // 렌탈 가격 계산

        // 포인트 부족 여부 확인
        if (currentPoints < rentalPrice) {
            throw new RuntimeException("포인트 부족");
        }

        // 포인트 차감 후 저장
        PointEntity pointUsage = PointEntity.builder()
                .memberId(rentalDTO.getMemberId())
                .usedPoint(rentalPrice)
                .addPoint(0)
                .currentPoint(currentPoints - rentalPrice) // 차감 후 저장
                .pointDateTime(LocalDateTime.now())
                .build();

        pointRepository.save(pointUsage);

        // 상품 정보 조회
        ProductEntity product = productRepository.findById(rentalDTO.getProductNo())
                .orElseThrow(() -> new RuntimeException("상품 정보 없음"));

        // 제공자 정보 추가
        String ownerNo = product.getOwnerNo();  // 상품의 제공자 번호

        // DTO에 제공자 번호 설정
        rentalDTO.setOwnerNo(ownerNo);

        // 재고 부족 체크 - 수량 증가 전에 확인!!
        int availableStock = product.getTotalStock() - product.getUsedStock();

        if (availableStock < rentalDTO.getRentalNumber()) {
            throw new RuntimeException("재고 부족");
        }

        // 사용 중 재고 증가
        productRepository.incrementUsedStock(product.getProductNo(), rentalDTO.getRentalNumber());

        // JPA 저장
        rentalRepository.save(modelMapper.map(rentalDTO, RentalEntity.class));
    }

    // 사용자 - 예약 조회(쿼리 DSL)
    public Page<UserOrderViewDTO> findRentalOrderListByUser(String memberId, String period, LocalDate searchDate, Criteria cri) {
        Pageable pageable = PageRequest.of(cri.getPageNum() - 1, cri.getAmount());
        return userRentalRepositoryCustom.findRentalOrderListByUser(memberId,period, searchDate, pageable);
    }

    // 사용자의 마이페이지 예약진행상태 카운트
    public List<RentalStateCountDTO> countRentalStatesByUser(String memberId) {
        return userRentalStateCountRepositoryCustom.countRentalStatesByUser(memberId);
    }

    // 사용자 사용중인 상품 조회 = 배송완료상태인 예약
    public Page<ActiveRentalDTO> findActiveRentalListByUser(String memberId, Criteria cri) {
        Pageable pageable = PageRequest.of(cri.getPageNum() - 1, cri.getAmount());
        return userActiveRentalRepositoryCustom.findActiveRentalListByUser(memberId, pageable);
    }

    // 사용자,제공자 예약 상세페이지
    public List<RentalDetailDTO> findRentalDetail(String rentalNo) {
        return detailRentalRepositoryCustom.findRentalDetail(rentalNo);
    }

    // 사용자 예약 배송지 변경
    @Transactional
    public void updateRentalDeliveryAddress(String rentalNo, int newDestinationNo) {

        RentalEntity rentalEntity = rentalRepository.findById(rentalNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        // 배송지(destinationNo) 변경
        rentalEntity.changeDestinationNo(newDestinationNo);
    }

    /* comment.-------------------------------------------- 관리자 -----------------------------------------------*/
    // 관리자 - 예약 조회(쿼리 DSL)
    public Page<AdminRentalViewDTO> findRentalAllListByAdmin(AdminRentalSearchCriteria criteria, Criteria cri) {
        Pageable pageable = PageRequest.of(cri.getPageNum() - 1, cri.getAmount());
        return adminRentalRepositoryCustom.findRentalAllListByAdmin(criteria, pageable);
    }

    public Page<AdminSalesDTO> getSalesByDate(String yearMonth, String storeName, Criteria cri) {
        Pageable pageable = PageRequest.of(cri.getPageNum() - 1, cri.getAmount());
        return adminSalesRepositoryCustom.findSalesByDate(yearMonth, storeName, pageable);
    }

/* comment.-------------------------------------------- 제공자 -----------------------------------------------*/

    // 제공자 - 예약 조회(쿼리 DSL)
    public Page<OwnerRentalViewDTO> findRentalListByOwner(String ownerNo, String period, String rentalTab, Criteria cri) {
        Pageable pageable = PageRequest.of(cri.getPageNum() - 1, cri.getAmount());  // 페이지 번호 조정
        return ownerRentalRepositoryCustom.findRentalListByOwner(ownerNo, period, rentalTab, pageable);
    }

    // 예약 확정
    @Transactional
    public void confirmRentals(List<String> rentalNos) {

        for (String rentalNo : rentalNos) {
            System.out.println("rentalNo = " + rentalNo);
            RentalEntity rental = rentalRepository.findByRentalNo(rentalNo)
                    .orElseThrow(() -> new IllegalArgumentException("해당 예약 정보를 찾을 수 없습니다: " + rentalNo));

            rental.changeRentalState("예약완료");  // 상태 변경
        }
    }

    // 예약취소
    @Transactional
    public void cancelBatch(String rentalNo, String rentalState) {

        RentalEntity rental = rentalRepository.findByRentalNo(rentalNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 정보를 찾을 수 없습니다: " + rentalNo));

        rental.changeRentalState(rentalState);  // Setter 대신 메서드 사용
    }

    // 예약진행상태 수정 (배송중 -> 배송완료, 수거중 -> 수거완료)
    @Transactional
    public void updateRentalState(String rentalNo) {

        RentalEntity rentalEntity = rentalRepository.findById(rentalNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다."));

        String currentState = rentalEntity.getRentalState();

        // 유효한 상태 변경인지 확인
        if ("배송중".equals(currentState)) {
            rentalEntity.changeRentalState("배송완료");

            // rentalTerm 조회
            RentalOptionInfoEntity rentalOption = rentalOptionInfoRepository.findById(rentalEntity.getRentalInfoNo())
                    .orElseThrow(() -> new IllegalArgumentException("해당 대여 옵션 정보가 존재하지 않습니다."));

            // 대여 기간 변경
            rentalEntity.changeRentalPeriod(LocalDateTime.now(), rentalOption.getRentalTerm());
        } else if ("수거중".equals(currentState)) {
            rentalEntity.changeRentalState("반납완료");
        } else if ("배송완료".equals(currentState)) {
            rentalEntity.changeRentalState("반납요청");
        } else {
            throw new IllegalStateException("잘못된 상태 변경 요청입니다.");
        }
    }

    // 운송장번호, 운송 업체명 수정 -> 배송중&수거중으로 상태 업데이트
    @Transactional
    public void updateDelivery(String rentalNo, String deliveryNo, String deliverCom) {
        RentalEntity rental = rentalRepository.findByRentalNo(rentalNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 정보를 찾을 수 없습니다: " + rentalNo));

        // 현재 예약 상태(rentalState) 확인
        String currentState = rental.getRentalState();

        // 상태에 따라 배송중 또는 수거중으로 변경
        if ("예약완료".equals(currentState)) {
            rental.changeRentalState("배송중");  // 배송중으로 상태 변경
        } else if ("반납요청".equals(currentState)) {
            rental.changeRentalState("수거중");  // 수거중으로 상태 변경
        }

        // 운송장 번호와 운송 업체명 업데이트
        rental.changeDelivery(deliveryNo, deliverCom);
    }

    // 제공자별 매출 현황 조회
    public List<OwnerSalesDTO> getSalesByOwner(String ownerNo, String yearMonth, String productNo) {
        return ownerSalesRepositoryCustom.getSalesByOwner(ownerNo, yearMonth, productNo);
    }
}
