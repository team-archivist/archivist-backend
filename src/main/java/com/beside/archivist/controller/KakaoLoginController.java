package com.beside.archivist.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.beside.archivist.service.KakaoService;
/*
import com.beside.archivist.service.HttpSessionOAuth2AuthorizedClientRepository;
import com.beside.archivist.service.KakaoService;
import com.beside.archivist.service.OAuth2User;
import com.beside.archivist.service.OAuth2UserRequest;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;*/



@Slf4j
@RestController
@RequestMapping("")
public class KakaoLoginController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${kakao.client_id}")
    private String client_id;

    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/sigin-callback")
    public String callback(@RequestParam("code") String code) throws IOException {
    	
    	System.out.println("code ====="+code);
        String accessToken = kakaoService.getAccessTokenFromKakao(client_id, code);
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        log.info("id : " + userInfo.get("id"));
        // User 데이터
        return userInfo.toString();
    }
    
    public String createAccessToken(long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofHours(2).toMillis());
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, /*Secrets.JWT_ACCESS_SECRET_KEY*/"??")
                .compact();
    }
    
    
	/*
	 * @Bean public OAuth2UserService<OAuth2UserRequest, OAuth2User>
	 * oauth2UserService(OAuth2AuthorizedClientRepository clientRepository) {
	 * DefaultOAuth2UserService delegate = new DefaultOAuth2UserService(); return
	 * request -> { OAuth2User user = delegate.loadUser(request); // 커스텀 로직 추가
	 * return user; }; }
	 * 
	 * @Bean public OAuth2AuthorizedClientRepository authorizedClientRepository() {
	 * return new HttpSessionOAuth2AuthorizedClientRepository(); }
	 */
}
