package com.ohgiraffers.funniture.chat.controller;

import com.ohgiraffers.funniture.adminInquiry.model.service.AdminInquiryService;
import com.ohgiraffers.funniture.chat.model.dto.ChatDTO;
import com.ohgiraffers.funniture.chat.model.service.ChatService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Chat API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    // 챗봇 질문 리스트 조회
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
    public ResponseEntity<ResponseMessage> getChatList(@RequestParam(required = false) Integer refNum, @RequestParam(required = false) Integer qaLevel){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        List<ChatDTO> result = chatService.getChatQuList(refNum, qaLevel);

        if (qaLevel != null && qaLevel >=2){
            List<ChatDTO> refResult = chatService.getRefChatQuList(qaLevel);
            responseMap.put("refResult", refResult);
        }

        responseMap.put("result", result);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "전체 질문 리스트 조회 성공", responseMap));
    }

    // 챗봇 질문 수정 & 등록
    @Operation(summary = "질문 리스트 수정 & 등록",
            description = "챗봇에서 사용할 질문 리스트 답변 등 수정 및 새로운 질문 등록"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "질문 리스트 수정 성공"),
            @ApiResponse(responseCode = "404", description = "수정 대상 못 찾음")
    })
    @PutMapping("modify")
    public ResponseEntity<ResponseMessage> modifyChatList(@RequestBody List<ChatDTO> updateList){
        System.out.println("updateList = " + updateList);

        boolean result = chatService.modifyChatList(updateList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (result == true){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "저장 성공", null));
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(404, "수정할 데이터를 찾지 못했습니다.", null));
    }

    // 챗봇 질문 삭제
    @Operation(summary = "질문 리스트 삭제",
            description = "챗봇에서 사용할 질문 삭제시 해당 하위 질문들 전부 삭제",
            parameters = {
                    @Parameter(name = "chatNo", description = "질문 번호")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "질문 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "삭제 대상 못 찾음")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteChatItem(@RequestParam Integer chatNo){
        boolean result =  chatService.deleteChatItem(chatNo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (result == true){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "삭제 성공", null));
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(404, "삭제할 데이터를 찾지 못했습니다.", null));
    }
}
