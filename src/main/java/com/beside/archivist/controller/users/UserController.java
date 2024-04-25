package com.beside.archivist.controller.users;


import com.beside.archivist.dto.users.KakaoLoginDto;
import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.UserImg;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.users.EmailTokenMismatchException;
import com.beside.archivist.exception.users.MissingAuthenticationException;
import com.beside.archivist.service.users.UserImgService;
import com.beside.archivist.service.users.UserService;
import com.beside.archivist.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.beside.archivist.service.users.KakaoService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final KakaoService kakaoServiceImpl;
    private final UserService userServiceImpl;
    private final JwtTokenUtil jwtTokenUtil;

    /** 카카오 로그인 - JWT 발급 */
    @PostMapping("/login/kakao")
    public ResponseEntity<?> callback(@RequestParam("code") String code){
        String accessToken = kakaoServiceImpl.getAccessTokenFromKakao(code);
        KakaoLoginDto response = kakaoServiceImpl.getUserInfo(accessToken);

        // 쿠키에 토큰 정보 추가- FE 요청
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "Token=" + response.getToken() + "; HttpOnly; Path=/; Max-Age=3600");

        return ResponseEntity.ok().headers(headers).body(response);
    }

    /** 관리자용 엔드 포인트 구현 **/
    @GetMapping("/api/admin")
    public ResponseEntity<?> adminLogin(@RequestParam String email, @RequestParam String password) {
        userServiceImpl.adminLogin(email,password);
        return ResponseEntity.ok().body("BE로 문의 주세요");
    }

    /** 회원 정보 저장 **/
    @PostMapping("/user")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto, @RequestHeader("Authorization") String tokenHeader) {
        // 토큰에서 추출한 이메일과 요청받은 이메일이 다를 경우 예외 발생
        String emailFromToken = jwtTokenUtil.getUsernameFromToken(tokenHeader.substring(7));
        if (!emailFromToken.equals(userDto.getEmail())) {
            throw new EmailTokenMismatchException(ExceptionCode.EMAIL_TOKEN_MISMATCH);
        }
        UserInfoDto savedUser = userServiceImpl.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /** 회원 정보 조회 **/
    @GetMapping("/user")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        UserInfoDto userInfo = userServiceImpl.getUserInfo(authentication.getName());
        return ResponseEntity.ok().body(userInfo);
    }

    /** 회원 정보 수정 **/
    @PatchMapping("/user/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId,
                                        @RequestPart("userDto") @Valid UserDto userDto,
                                        @RequestPart(value = "userImgFile", required = false) MultipartFile userImgFile) {
        UserInfoDto updatedUser = userServiceImpl.updateUser(userId, userDto, userImgFile);
        return ResponseEntity.ok().body(updatedUser);
    }

    /** 회원 탈퇴 **/
    @DeleteMapping("/user/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId){
        userServiceImpl.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /** 카테고리 모든 값 조회 **/
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok().body(Category.values());
    }

    /** 정의되어있는 모든 닉네임 조회 **/
    @GetMapping("/nicknames")
    public ResponseEntity<?> getNicknames() {
        List<String> nicknames = userServiceImpl.getNicknames();
        return ResponseEntity.ok().body(nicknames);
    }

}
