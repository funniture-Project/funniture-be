package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.cloudinary.CloudinaryService;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dto.AppOwnerInfoDTO;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO;
import com.ohgiraffers.funniture.member.model.service.MemberService;
import com.ohgiraffers.funniture.product.model.dto.ProductDTO;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        System.out.println("화면에서 넘어온 memberId"+ memberId);
        System.out.println("✅ memberList 조회를 위한 memberList 컨트롤러 동작..");

        MemberDTO memberDTO = memberService.getMemberList(memberId);
        System.out.println("✅ 서비스에서 넘어온 로그인 회원 목록 = " + memberDTO);

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

        System.out.println("프론트에서 패스워드 변경 요청 잘 들어 왔나? email = " + memberDTO.getEmail());
        System.out.println("프론트에서 패스워드 변경 요청 잘 들어 왔나? password = " + memberDTO.getPassword());

        MemberEntity memberEntity = memberService.findByEmail(memberDTO.getEmail());
        System.out.println("이메일에 해당하는 값이 있나? memberEntity = " + memberEntity);

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
        System.out.println(" 서버에 잘 들어왔나 memberDTO = " + memberDTO);

        String memberId = memberDTO.getMemberId();
        String password = memberDTO.getPassword();
        System.out.println("memberId = " + memberId);
        System.out.println("password = " + password);

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
        System.out.println("전화번호 변경 로직 서버에 잘 들어왔나 = " + memberDTO);

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
        System.out.println("비밀번호 변경 로직 서버에 잘 들어왔나 = " + memberDTO);

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
        System.out.println("주소 변경 로직 서버에 잘 들어왔나 = " + memberDTO);

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

        System.out.println("memberDTO = " + memberDTO);
        System.out.println("file = " + file);

        String memberId = memberDTO.getMemberId();
        MemberEntity memberEntity = memberService.findByMemberId(memberId);

        // Cloudinary에 파일 업로드
        Map<String, Object> response = cloudinaryService.uploadFile(file);
        System.out.println("response = " + response);

        memberDTO.setImageLink(response.get("url").toString());
//        memberDTO.setMemberId(response.get("id").toString());

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
        System.out.println("최초 프론트에서 회원 탈퇴 요청 들왔나 memberId = " + memberId);

        MemberDTO result = memberService.withdrawService(memberId);

        System.out.println("result.getMemberRole() = " + result.getMemberRole());
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

        System.out.println("제공자 전환 신청 잘 들어왔는지 = " + appOwnerInfoDTO);

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
        boolean isRegistered = memberService.existsByMemberId(memberId);
        System.out.println("제공자 전환 신청 여부 확인 컨트롤러 - memberId: " + memberId + ", isRegistered: " + isRegistered);

        Map<String, Object> response = new HashMap<>();
        response.put("isRegistered", isRegistered);

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

}
