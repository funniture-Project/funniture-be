package com.ohgiraffers.funniture.adminInquiry.controller;

import com.ohgiraffers.funniture.adminInquiry.model.service.AdminInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/adinquiry")
public class AdminInquiryController {

    private final AdminInquiryService adService;




}
