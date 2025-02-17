package com.ohgiraffers.funniture.deliveryaddress.controllers;

import com.ohgiraffers.funniture.deliveryaddress.model.dto.DeliveryAddressDTO;
import com.ohgiraffers.funniture.deliveryaddress.model.service.DeliveryAddressService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "DeliveryAddress API")
@RestController
@RequestMapping("/api/v1/deliveryaddress")
@RequiredArgsConstructor
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @Operation(summary = "사용자별 배송지 조회",
            description = "사용자 배송지 관리 페이지 & 주문/배송 페이지 & 예약 등록 페이지 & 반납 등록 페이지에서 사용",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID(필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "배송지 내역이 없습니다."),
            @ApiResponse(responseCode = "200", description = "배송지 조회 완료")
    })
    @GetMapping
    public ResponseEntity<ResponseMessage> findDeliveryAddressByUser(@RequestParam String memberId){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<DeliveryAddressDTO> addressList = deliveryAddressService.findDeliveryAddressByUser(memberId);

        if (addressList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "배송지 내역이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("addressList", addressList);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "배송지 조회 완료", res));
    }




}
