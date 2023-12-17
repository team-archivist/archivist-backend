package com.beside.archivist.controller.users;


import com.beside.archivist.dto.users.KakaoLoginDto;
import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.entity.users.UserImg;
import com.beside.archivist.service.users.UserImgService;
import com.beside.archivist.service.users.UserService;
import com.beside.archivist.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.beside.archivist.service.users.KakaoService;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final KakaoService kakaoServiceImpl;
    private final UserService userServiceImpl;
    private final UserImgService userImgServiceImpl;

    /** 카카오 로그인 - JWT 발급 */
    @PostMapping("/login/kakao")
    public ResponseEntity<?> callback(@RequestParam("code") String code){
        String accessToken = kakaoServiceImpl.getAccessTokenFromKakao(code);
        String response = kakaoServiceImpl.getUserInfo(accessToken);
        return ResponseEntity.ok().body(response);
    }

    /** 관리자용 엔드 포인트 구현 **/
    @GetMapping("/api/admin")
    public ResponseEntity<?> adminLogin(@RequestParam String email, @RequestParam String password) {
        userServiceImpl.adminLogin(email,password);
        return ResponseEntity.ok().body("BE로 문의 주세요");
    }

    /** 회원 정보 저장 **/
    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        UserImg userImg = userImgServiceImpl.initializeDefaultImg();
        UserInfoDto savedUser = userServiceImpl.saveUser(userDto,userImg);
        return ResponseEntity.ok().body(savedUser);
    }

    /** 회원 정보 조회 **/
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") Long userId) {
        UserInfoDto userInfo = userServiceImpl.getUserInfo(userId);
        return ResponseEntity.ok().body(userInfo);
    }

    /** 회원 정보 수정 **/
    @PostMapping("/user/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId,
                                        @RequestPart("userDto") UserDto userDto,
                                        @RequestPart(value = "userImgFile", required = false) MultipartFile userImgFile) {
        UserInfoDto updatedUser = userServiceImpl.updateUser(userId, userDto, userImgFile);
        return ResponseEntity.ok().body(updatedUser);
    }

    /** 회원 탈퇴 **/
    @DeleteMapping("/user/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId){
        userServiceImpl.deleteUser(userId);
        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }
}
