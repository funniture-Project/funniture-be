package com.ohgiraffers.funniture.deliveryaddress.model.service;

import com.ohgiraffers.funniture.deliveryaddress.entity.DeliveryAddressEntity;
import com.ohgiraffers.funniture.deliveryaddress.model.dao.DeliveryAddressRepository;
import com.ohgiraffers.funniture.deliveryaddress.model.dto.DeliveryAddressDTO;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final ModelMapper modelMapper;

    public List<DeliveryAddressDTO> findDeliveryAddressByUser(String memberId) {

        List<DeliveryAddressEntity> addressList = deliveryAddressRepository.findDeliveryAddressByUser(memberId);

        return addressList.stream().map(address -> modelMapper.map(address, DeliveryAddressDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public void deliveryAddressRegist(DeliveryAddressDTO deliveryAddressDTO) {

        // Entity 변환
        DeliveryAddressEntity deliveryAddressEntity = modelMapper.map(deliveryAddressDTO, DeliveryAddressEntity.class);

        if (deliveryAddressDTO.getIsDefault() != null && deliveryAddressDTO.getIsDefault() == 1) {
            // 새로운 주소가 기본 배송지로 등록하려는 경우
            // 먼저, 해당 회원의 기본 배송지가 있는지 확인
            Optional<DeliveryAddressEntity> existingDefaultAddress =
                    deliveryAddressRepository.findByMemberIdAndIsDefaultTrue(deliveryAddressDTO.getMemberId());

            // 기본 배송지가 존재하면 기본 배송지를 false로 설정
            existingDefaultAddress.ifPresent(address -> {
                // 기존 기본 배송지를 false로 업데이트
                address.setDefault(false);
                deliveryAddressRepository.save(address);
            });

            // 새로 등록되는 배송지는 기본 배송지로 설정
            deliveryAddressEntity.setDefault(true);
        }

        deliveryAddressRepository.save(modelMapper.map(deliveryAddressDTO, DeliveryAddressEntity.class));
    }

    @Transactional
    public void deliveryAddressUpdate(DeliveryAddressDTO deliveryAddressDTO) {

        // 수정할 배송지 찾기
        DeliveryAddressEntity deliveryAddressEntity =
                deliveryAddressRepository.findById(deliveryAddressDTO.getDestinationNo())
                        .orElseThrow(() -> new IllegalArgumentException("해당 배송지가 존재하지 않습니다."));

        // 만약 사용자가 기본 배송지를 설정하려는 경우
        if (deliveryAddressDTO.getIsDefault() != null && deliveryAddressDTO.getIsDefault() == 1) {
            // 해당 회원(memberId)의 기존 기본 배송지 찾기
            Optional<DeliveryAddressEntity> existingDefaultAddress =
                    deliveryAddressRepository.findByMemberIdAndIsDefaultTrue(deliveryAddressEntity.getMemberId());

            // 기존 기본 배송지가 있다면 기본 배송지 해제 (isDefault = false)
            existingDefaultAddress.ifPresent(address -> {
                DeliveryAddressEntity updatedOldDefault = address.toBuilder().isDefault(false).build();
                deliveryAddressRepository.save(updatedOldDefault);
            });


        }

        // 새로운 수정된 배송지 생성 및 저장
        DeliveryAddressEntity updateDeliveryAddress = deliveryAddressEntity.toBuilder()
                .destinationName(deliveryAddressDTO.getDestinationName())   // 배송지 이름 수정
                .destinationPhone(deliveryAddressDTO.getDestinationPhone()) // 배송지 전화번호 수정
                .destinationAddress(deliveryAddressDTO.getDestinationAddress()) // 배송지 수정
                .receiver(deliveryAddressDTO.getReceiver())                 // 받는 이 수정
                .isDefault(deliveryAddressDTO.getIsDefault() != null && deliveryAddressDTO.getIsDefault() == 1) // 기본배송지 여부 수정
                .build();

        deliveryAddressRepository.save(updateDeliveryAddress);
    }
}
