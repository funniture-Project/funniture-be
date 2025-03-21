package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.cloudinary.CloudinaryService;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dto.AppOwnerInfoDTO;
import com.ohgiraffers.funniture.member.model.dto.ConnectCountDTO;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO;
import com.ohgiraffers.funniture.member.model.service.CountNumService;
import com.ohgiraffers.funniture.member.model.service.MemberService;
import com.ohgiraffers.funniture.product.model.dto.ProductDTO;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "LOGIN API")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthController authController;
    private final CloudinaryService cloudinaryService;
    private final CountNumService countNumService;

    @Operation(summary = "로그인 회원 정보 조회",
            description = "로그인 시, 로그인 한 회원에 대한 정보 조회",
            parameters = {
                    @Parameter(name = "memberId", description = "회원 ID를 이용하여 정보 조회"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보 조회 실패")
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMessage> memberList (@PathVariable String memberId) {

        MemberDTO memberDTO = memberService.getMemberList(memberId);

        memberDTO.setPassword(null);
        Map<String , Object> result = new HashMap<>();
        result.put("result" , memberDTO);

        if (memberDTO == null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "회원 정보가 존재하지 않습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "로그인 한 회원 목록 조회 성공", result));
    }

    @Operation(summary = "로그인 페이지 비밀번호 변경",
            description = "로그인 페이지에서 비밀번호 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호 변경 실패")
    })
    @PostMapping ("/findPass")
    public ResponseEntity<ResponseMessage> changePasswordByLogin (@RequestBody MemberDTO memberDTO) {

        MemberEntity memberEntity = memberService.findByEmail(memberDTO.getEmail());

        // 서비스로 비밀번호 바꿀 정보 넘겨주기
        MemberEntity result = memberService.changePassword(memberEntity, memberDTO.getPassword());

        if(result.getPassword() != null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201 , "비밀번호 변경 완료", null));
        } else {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400 , "비밀번호 변경 실패", null));
        }
    }

    @Operation(summary = "사용자 본인 인증",
            description = "사용자 마이페이지에서 비밀번호 인증")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "비밀번호 인증 성공"),
            @ApiResponse(responseCode = "404", description = "비밀번호 인증 실패")
    })
    @PostMapping("/conform")
    public ResponseEntity<ResponseMessage> confirmToMyPage (@RequestBody MemberDTO memberDTO) {

        String memberId = memberDTO.getMemberId();
        String password = memberDTO.getPassword();

        boolean result = memberService.comparePassword(memberId, password);

        if (result) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(200,"비밀번호 인증 성공", null));
        } else {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404,"비밀번호 인증 실패", null));
        }
    }

    @Operation(summary = "사용자 휴대전화 번호 변경",
            description = "사용자 마이페이지에서 휴대전화 번호 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "휴대전화 번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "휴대전화 번호 변경 실패")
    })
    @PutMapping("modify/phone")
    public ResponseEntity<ResponseMessage> modifyPhoneNumber (@RequestBody MemberDTO memberDTO) {

        String memberId = memberDTO.getMemberId();
        String phoneNumber = memberDTO.getPhoneNumber();
        MemberEntity memberEntity = memberService.changePhoneNumber(memberId, phoneNumber);

        if (memberEntity != null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201,"휴대전화 번호 변경 성공", null));
        } else {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400,"휴대전화 번호 변경 실패", null));
        }
    }

    @Operation(summary = "사용자 비밀번호 변경",
            description = "사용자 마이페이지에서 비밀번호 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호 변경 실패")
    })
    @PutMapping("modify/password")
    public ResponseEntity<ResponseMessage> modifyPassword (@RequestBody MemberDTO memberDTO) {

        String memberId = memberDTO.getMemberId();
        String password = memberDTO.getPassword();

        MemberEntity memberEntity = memberService.findByMemberId(memberId);
        MemberEntity result = memberService.changePasswordByMypage(memberEntity, password);

        if(result.getPassword() != null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201 , "비밀번호 변경 완료", null));
        } else {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400 , "비밀번호 변경 실패", null));
        }
    }

    @Operation(summary = "사용자 주소 변경",
            description = "사용자 마이페이지에서 주소 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주소 변경 성공"),
            @ApiResponse(responseCode = "400", description = "주소 변경 실패")
    })
    @PutMapping("modify/address")
    public ResponseEntity<ResponseMessage> modifyAddress (@RequestBody MemberDTO memberDTO) {

        String memberId = memberDTO.getMemberId();
        String address = memberDTO.getAddress();

        MemberEntity memberEntity = memberService.findByMemberId(memberId);
        MemberEntity result = memberService.changeAddressByMypage(memberEntity, address);

        if(result.getPassword() != null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201 , "주소 변경 완료", null));
        } else {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400 , "주소 변경 실패", null));
        }
    }

    // 프로필 사진 변경
    @Operation(summary = "프로필 사진 변경",
            description = "사용자 페이지에서 프로필 사진 변경"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400",description = "프로필 사진 변경 실패."),
            @ApiResponse(responseCode = "201", description = "프로필 사진 변경 성공")
    })
    @PutMapping(value = "modify/imageLink", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> modifyImageLink(
            @Valid @RequestPart (value = "formData") MemberDTO memberDTO,
            @RequestPart(value = "imageLink") MultipartFile file) { // 파일 데이터

        String memberId = memberDTO.getMemberId();
        memberService.findByMemberId(memberId);

        // Cloudinary에 파일 업로드
        Map<String, Object> response = cloudinaryService.uploadFile(file);

        memberDTO.setImageLink(response.get("url").toString());

        memberService.updateMemberImage(memberDTO);

        if (response != null) {

            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201, "프로필 사진 변경 성공", null));

            // DB 업데이트 로직 수행
        } else {
            System.out.println("파일이 없습니다.");
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400, "프로필 사진 변경 실패", null));
        }
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴",
            description = "사용자 페이지에서 회원 탈퇴(권한 변경 LIMIT)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400",description = "회원 탈퇴 실패.")
    })
    @PutMapping("/withdraw/{memberId}")
    public ResponseEntity<ResponseMessage> withdrawByMemberId(@PathVariable String memberId){

        MemberDTO result = memberService.withdrawService(memberId);

        if (result.getMemberRole() == "LIMIT"){
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201, "회원 탈퇴 성공", null));
        } else{
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400, "회원 탈퇴 실패", null));
        }
    }

    // 제공자 전환 신청
    @Operation(summary = "제공자 전환 신청", description = "제공자 전환 신청 데이터를 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "제공자 전환 신청 성공"),
            @ApiResponse(responseCode = "400", description = "제공자 전환 신청 실패")
    })
    @PostMapping(value = "/owner/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> registerOwner(
            @RequestPart("ownerData") @Valid AppOwnerInfoDTO appOwnerInfoDTO,
            @RequestPart(value = "storeImage", required = false) MultipartFile storeImage,
            @RequestPart(value = "attachmentFile", required = false) MultipartFile attachmentFile) {

        try {
            // Cloudinary에 이미지 업로드
            if (storeImage != null && !storeImage.isEmpty()) {
                Map<String, Object> imageResponse = cloudinaryService.uploadFile(storeImage);
                appOwnerInfoDTO.setStoreImage(imageResponse.get("url").toString());
            }

            // Cloudinary에 첨부파일 업로드
            if (attachmentFile != null && !attachmentFile.isEmpty()) {
                Map<String, Object> fileResponse = cloudinaryService.uploadPdfFile(attachmentFile);
                appOwnerInfoDTO.setAttechmentLink(fileResponse.get("url").toString());
            }

            // 서비스 호출 후 저장된 데이터를 가져옴
            AppOwnerInfoDTO savedOwnerInfo = memberService.registerOwner(appOwnerInfoDTO);

            Map <String , Object> result = new HashMap<>();
            result.put("result", savedOwnerInfo);

            // 응답 객체 생성 및 반환
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201, "제공자 전환 신청 성공", result));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400, "제공자 전환 신청 실패", null));
        }
    }


    // 기존 제공자 신청 여부 확인하기 ( 여기에는 400 을 작성하면 에러가 발생해서
    @Operation(summary = "제공자 전환 신청 여부 확인",
            description = "제공자 전환 재신청 시 최초 신청인지 재신청인지 여부 확인",
            parameters = {
                    @Parameter(name = "memberId", description = "회원 ID를 이용하여 제공자 신청여부 확인"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제공자 전환 신청 여부 확인 성공")
    })
    @GetMapping("/owner/status/{memberId}")
    public ResponseEntity<ResponseMessage> checkOwnerStatus(@PathVariable String memberId) {
//        boolean isRegistered = memberService.existsByMemberId(memberId);
        String status = memberService.getOwnerStatus(memberId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", status);

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "제공자 전환 신청 여부 확인 성공", response));
    }

    // 제공자 전환 재신청 (update)
    @Operation(summary = "제공자 전환 재신청", description = "제공자 전환 재신청 시 데이터를 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "제공자 전환 재신청 성공"),
            @ApiResponse(responseCode = "400", description = "제공자 전환 재신청 실패")
    })
    @PostMapping(value = "/owner/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseMessage> upsertOwner(
            @RequestPart("ownerData") @Valid AppOwnerInfoDTO appOwnerInfoDTO,
            @RequestPart(value = "storeImage", required = false) MultipartFile storeImage,
            @RequestPart(value = "attachmentFile", required = false) MultipartFile attachmentFile) {

        try {
            // Cloudinary에 이미지 업로드
            if (storeImage != null && !storeImage.isEmpty()) {
                Map<String, Object> imageResponse = cloudinaryService.uploadFile(storeImage);
                appOwnerInfoDTO.setStoreImage(imageResponse.get("url").toString());
            }

            // Cloudinary에 첨부파일 업로드
            if (attachmentFile != null && !attachmentFile.isEmpty()) {
                Map<String, Object> fileResponse = cloudinaryService.uploadPdfFile(attachmentFile);
                appOwnerInfoDTO.setAttechmentLink(fileResponse.get("url").toString());
            }

            // 서비스 호출 후 저장된 데이터를 가져옴
            AppOwnerInfoDTO savedOwnerInfo = memberService.upsertOwner(appOwnerInfoDTO);

            Map <String , Object> result = new HashMap<>();
            result.put("result", savedOwnerInfo);

            // 응답 객체 생성 및 반환
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201, "제공자 정보 저장 성공", result));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400, "제공자 정보 저장 실패", null));
        }
    }

    // 재공자 신청 반려 됐을 때 반려 메시지 가져오는 애)
    @Operation(summary = "제공자 전환 반려 메시지", description = "제공자 전환 반려 됐을 때 반려 메시지 불러오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제공자 전환 반려 메시지 조회 성공"),
            @ApiResponse(responseCode = "404", description = "제공자 전환 반려 메시지 조회 실패")
    })
    @GetMapping(value = "/rejected/{memberId}")
    public ResponseEntity<ResponseMessage> rejectedMessageMypage(@PathVariable String memberId)  {

        MemberDTO memberDTO = memberService.getRejectedMessage(memberId);

        Map<String , Object> result = new HashMap<>();
        result.put("result" , memberDTO);

        if (memberDTO == null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "제공자 전환 반려 메시지가 존재하지 않습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "제공자 전환 반려 메시지 조회 성공", result));
    }

    // 재공자 전환 신청에서 중복된 store_no 있는지 확인하는 로직
    @Operation(summary = "제공자 전환 중복 데이터 확인", description = "제공자 전환 시 중복 데이터 여부 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제공자 전환 사업자등록번호 조회 성공"),
            @ApiResponse(responseCode = "400", description = "제공자 전환 사업자등록번호 중복")
    })
    @GetMapping("/check-store-no")
    public ResponseEntity<ResponseMessage> checkStoreNoDuplicate(@RequestParam String storeNo, @RequestParam String memberId) {
        boolean isDuplicateOrOwned = memberService.isStoreNoDuplicateOrOwnedByUser(storeNo, memberId);

        if (isDuplicateOrOwned) {
            return ResponseEntity.ok(new ResponseMessage(400, "중복된 사업자 번호입니다.", null));
        } else {
            return ResponseEntity.ok(new ResponseMessage(200, "사용 가능한 사업자 번호입니다.", null));
        }
    }

    @Operation(summary = "관리자와 상담 상태 변경",
            description = "관리자와의 상담 상태 변경",
            parameters = {
                    @Parameter(name = "memberId", description = "회원번호")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "변경 성공"),
            @ApiResponse(responseCode = "404", description = "해당 사용자 못찾음")
    })
    @PutMapping("/modify/consulting")
    public ResponseEntity<ResponseMessage> modifyConsulting(@RequestParam String memberId){
        System.out.println("대상 memberId = " + memberId);
        boolean result = memberService.modifyConsulting(memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType( "application","json", Charset.forName("UTF-8")));

        if (!result){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(404, "변경 대상 사용자 못 찾음", null));
        }

        MemberEntity findMember = memberService.findByMemberId(memberId);
        System.out.println("findMember = " + findMember);

        Map<String,Object> resultMap = new HashMap<>();

        resultMap.put("isConsulting",findMember.getIsConsulting());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(204, "변경 성공", resultMap));
    }

    @Operation(summary = "접속자 수 업데이트",
            description = "로그인과 동시에 권한별 접속자 수 증가",
            parameters = {
                    @Parameter(name = "mapRole", description = "회원권한")
            }
    )
    @PostMapping("/updateCount")
    public void updateCount(@RequestBody Map<String, String> mapRole){
        String role = mapRole.get("role");

        countNumService.updateCount(role);
    }

    @Operation(summary = "이번달 접속자 수 조회",
            description = "이번달 접속자 수 조회"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이번달 접속자 기록 조회 성공"),
            @ApiResponse(responseCode = "204", description = "이번달 접속자 기록 없음")
    })
    @GetMapping("/connectCount")
    public ResponseEntity<ResponseMessage> getConnectCount(){
       List<ConnectCountDTO> result = countNumService.getThisMonth();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType( "application","json", Charset.forName("UTF-8")));
        Map<String,Object> resultMap = new HashMap<>();

        if (result == null){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "이번달 접속자 기록 없음", null));
        }else {
            resultMap.put("result",result);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(200, "이번달 접속자 기록", resultMap));
        }
    }

}
