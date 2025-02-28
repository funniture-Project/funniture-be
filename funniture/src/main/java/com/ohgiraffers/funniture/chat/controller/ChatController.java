package com.ohgiraffers.funniture.chat.controller;

import com.ohgiraffers.funniture.adminInquiry.model.service.AdminInquiryService;
import com.ohgiraffers.funniture.chat.model.service.ChatService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    // 상세 정보 조회 (상품 상세 페이지, 제공자 상품 수정)
    @Operation(summary = "질문 리스트 조회",
            description = "챗봇에서 사용할 질문 리스트 조회",
            parameters = {
                    @Parameter(name = "refNum", description = "상위질문 번호"),
                    @Parameter(name = "qaLevel", description = "질문 레벨")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문 리스트 정보 조회 성공")
    })
    @GetMapping("/list")
    public void getChatList(@RequestParam(required = false) Integer refNum,@RequestParam(required = false) Integer qaLevel){

        System.out.println("refNum = " + refNum);
        System.out.println("qaLevel = " + qaLevel);

        chatService.getChatQuList(refNum, qaLevel);

    }


}
