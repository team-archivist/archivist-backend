package com.beside.archivist.controller.users;


import com.beside.archivist.dto.users.KakaoLoginDto;
import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.service.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.beside.archivist.service.users.KakaoService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final KakaoService kakaoServiceImpl;
    private final UserService userServiceImpl;

    @PostMapping("/login/kakao")
    public ResponseEntity<?> callback(@RequestParam("code") String code){
        String accessToken = kakaoServiceImpl.getAccessTokenFromKakao(code);
        KakaoLoginDto userInfo = kakaoServiceImpl.getUserInfo(accessToken);
        return ResponseEntity.ok().body(userInfo); // User 데이터
    }

    /** 관리자용 엔드 포인트 구현 **/
    @GetMapping("/api/admin")
    public ResponseEntity<?> adminLogin(@RequestParam String email, @RequestParam String password) {
        userServiceImpl.adminLogin(email,password);
        return ResponseEntity.ok().body("Success");
    }

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        User savedUser = userServiceImpl.saveUser(userDto);
        return ResponseEntity.ok().body(savedUser);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        User updatedUser = userServiceImpl.updateUser(userId,userDto);
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId){
        userServiceImpl.deleteUser(userId);
        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }
}
