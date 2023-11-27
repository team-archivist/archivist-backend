package com.beside.archivist.service;

import com.beside.archivist.dto.KakaoLoginDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service @Transactional
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService{

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.access-token-uri}")
    private String accessTokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    private final WebClient webClient;


    /** 1. 인가 코드로 카카오 에 토큰 요청 **/
    public String getAccessTokenFromKakao(String code) throws IOException {
        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id",clientId );
        params.add("code", code);

        String response = webClient.post()
                .uri(accessTokenUri)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(response, new TypeReference<>() {});

        return (String) jsonMap.get("access_token");
    }

    /** 2. 토큰으로 클라이언트 정보 요청 **/
    public KakaoLoginDto getUserInfo(String accessToken) throws IOException {
        // 클라이언트 요청 정보
        String response = webClient.get()
                    .uri(userInfoUri)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();


        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(response, new TypeReference<>() {});
        // 사용자 정보 추출
        Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
        // userInfo에 넣기
        KakaoLoginDto kakaoUser = KakaoLoginDto.builder()
                .id(properties.get("nickname").toString())
                .nickname(properties.get("profile_image").toString())
                .build();

        return kakaoUser;
    }
}
