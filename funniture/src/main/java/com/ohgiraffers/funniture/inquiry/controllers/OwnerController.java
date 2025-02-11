package com.ohgiraffers.funniture.inquiry.controllers;

import com.ohgiraffers.funniture.inquiry.model.service.InquiryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

@Tag(name = "OWNER API")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class OwnerController {

    private final InquiryService inquiryService;

    private HttpHeaders headersMethod () {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return headers;
    }

}
