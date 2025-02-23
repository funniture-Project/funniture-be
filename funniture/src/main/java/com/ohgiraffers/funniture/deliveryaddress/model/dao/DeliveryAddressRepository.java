package com.ohgiraffers.funniture.deliveryaddress.model.dao;

import com.ohgiraffers.funniture.deliveryaddress.entity.DeliveryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddressEntity, Integer> {

    @Query("SELECT d FROM DeliveryAddressEntity d WHERE d.memberId = :memberId and d.destinationStatus = '활성화'")
    List<DeliveryAddressEntity> findDeliveryAddressByUser(String memberId);

    Optional<DeliveryAddressEntity> findByMemberIdAndIsDefaultTrue(String memberId);

    Optional<DeliveryAddressEntity> findTopByMemberIdAndDestinationStatusOrderByCreatedAtDesc(
            String memberId, String destinationStatus);

}
