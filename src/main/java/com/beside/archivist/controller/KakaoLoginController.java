package com.beside.archivist.controller;


import com.beside.archivist.dto.KakaoLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;


import com.beside.archivist.service.KakaoService;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoService kakaoServiceImpl;

    @GetMapping("/login/kakao")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws IOException {
        String accessToken = kakaoServiceImpl.getAccessTokenFromKakao(code);
        KakaoLoginDto userInfo = kakaoServiceImpl.getUserInfo(accessToken);
        return ResponseEntity.ok().body(userInfo); // User 데이터
    }

    /* TEST 인가 코드 받는 엔드 포인트 */
    @GetMapping("/sigin-callback")
    public String callback2(@RequestParam("code") String code) {
        return code;
    }
}
