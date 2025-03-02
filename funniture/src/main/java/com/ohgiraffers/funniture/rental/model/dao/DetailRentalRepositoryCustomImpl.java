package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.deliveryaddress.entity.QDeliveryAddressEntity;
import com.ohgiraffers.funniture.member.entity.QMemberEntity;
import com.ohgiraffers.funniture.member.entity.QOwnerInfoEntity;
import com.ohgiraffers.funniture.product.entity.QProductEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QDetailRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.RentalDetailDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DetailRentalRepositoryCustomImpl implements DetailRentalRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RentalDetailDTO> findRentalDetail(String rentalNo) {
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QRentalOptionInfoEntity rentalOptionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QDeliveryAddressEntity deliveryAddress = QDeliveryAddressEntity.deliveryAddressEntity;
        QProductEntity product = QProductEntity.productEntity;
        QDetailRentalEntity rental = QDetailRentalEntity.detailRentalEntity;
        QMemberEntity member = QMemberEntity.memberEntity;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(rental.rentalNo.eq(rentalNo));

        return jpaQueryFactory
                .select(Projections.constructor(RentalDetailDTO.class,
                        rental.rentalNo,
                        rental.orderDate,
                        rental.rentalNumber,
                        rental.rentalState,
                        rental.deliveryMemo,
                        ownerInfo.storeName,
                        product.productName,
                        rentalOptionInfo.rentalPrice,
                        rentalOptionInfo.rentalTerm,
                        rentalOptionInfo.asNumber,
                        deliveryAddress.destinationName,
                        deliveryAddress.destinationPhone,
                        deliveryAddress.destinationAddress,
                        deliveryAddress.receiver,
                        member.email,
                        member.userName,
                        member.phoneNumber))
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.ownerInfoEntity, ownerInfo)
                .join(rental.rentalOptionInfoEntity, rentalOptionInfo)
                .join(rental.deliveryAddressEntity, deliveryAddress)
                .join(rental.memberEntity, member)
                .where(whereCondition)
                .fetch();
    }
}
