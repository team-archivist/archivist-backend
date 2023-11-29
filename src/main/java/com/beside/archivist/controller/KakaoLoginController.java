package com.beside.archivist.controller;


import com.beside.archivist.dto.KakaoLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.beside.archivist.service.KakaoService;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoService kakaoServiceImpl;

    @PostMapping("/login/kakao")
    public ResponseEntity<?> callback(@RequestParam("code") String code){
        String accessToken = kakaoServiceImpl.getAccessTokenFromKakao(code);
        KakaoLoginDto userInfo = kakaoServiceImpl.getUserInfo(accessToken);
        return ResponseEntity.ok().body(userInfo); // User 데이터
    }
}
