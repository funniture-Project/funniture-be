package com.ohgiraffers.funniture.deliveryaddress.controllers;

import com.ohgiraffers.funniture.deliveryaddress.model.dto.DeliveryAddressDTO;
import com.ohgiraffers.funniture.deliveryaddress.model.service.DeliveryAddressService;
import com.ohgiraffers.funniture.response.ResponseMessage;
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


    @GetMapping
    public ResponseEntity<ResponseMessage> findDeliveryAddressByUser(@RequestParam String memberId){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<DeliveryAddressDTO> addressList = deliveryAddressService.findDeliveryAddressByUser(memberId);

        Map<String, Object> res = new HashMap<>();
        res.put("addressList", addressList);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "배송지 조회 완료", res));
    }




}
