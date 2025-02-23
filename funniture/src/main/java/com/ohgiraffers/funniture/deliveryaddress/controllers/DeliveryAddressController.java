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
    // 사용자별 배송지 조회
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

    @Operation(summary = "사용자 신규 배송지 등록",
            description = "예약 등록 페이지, 마이페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신규배송지 등록이 완료되었습니다.")
    })
    // 신규 배송지 등록
    @PostMapping("/regist")
    public ResponseEntity<ResponseMessage> deliveryAddressRegist(@RequestBody DeliveryAddressDTO deliveryAddressDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        deliveryAddressService.deliveryAddressRegist(deliveryAddressDTO);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(201, "신규배송지 등록이 완료되었습니다.", null));
    }

    @Operation(summary = "사용자 배송지 수정",
            description = "예약 등록 페이지, 마이페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송지 수정이 완료되었습니다.")
    })
    // 배송지 수정
    @PutMapping("/update")
    public ResponseEntity<ResponseMessage> deliveryAddressUpdate(@RequestBody DeliveryAddressDTO deliveryAddressDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        deliveryAddressService.deliveryAddressUpdate(deliveryAddressDTO);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "배송지 수정이 완료되었습니다.", null));
    }

    @Operation(summary = "배송지 비활성화",
            description = "예약 등록 페이지, 마이페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송지가 비활성화되었습니다.")
    })
    // 사용자 배송지 삭제(사용자 입장에서 안보이게하기 -> 과거 예약에서 배송지가 확인 되어야하기때문에)
    @PutMapping("/delete/{destinationNo}")
    public ResponseEntity<ResponseMessage> deliveryAddressDelete(@PathVariable int destinationNo){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 서비스에서 배송지 상태를 '비활성화'로 변경
        deliveryAddressService.deliveryAddressDelete(destinationNo);

        return ResponseEntity.ok(new ResponseMessage(200, "배송지가 비활성화되었습니다.", null));
    }




}
