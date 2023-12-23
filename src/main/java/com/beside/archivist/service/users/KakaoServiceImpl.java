package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.KakaoLoginDto;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.repository.users.UserRepository;
import com.beside.archivist.utils.JwtTokenUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private final Type type = new TypeToken<Map<String, Object>>(){}.getType();

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    /** 1. 인가 코드로 카카오 에 토큰 요청 **/
    @Override
    public String getAccessTokenFromKakao(String code){
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

        Gson gson = new Gson();
        Map<String,Object> jsonMap = gson.fromJson(response, type);
        return jsonMap.get("access_token").toString();
    }

    /** 2. 토큰으로 클라이언트 정보 요청 **/
    @Override
    public String getUserInfo(String accessToken){
        // 클라이언트 요청 정보
        String response = webClient.get()
                    .uri(userInfoUri)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        Gson gson = new Gson();
        Map<String,Object> jsonMap = gson.fromJson(response, type);
        // 사용자 정보 추출
        Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) jsonMap.get("kakao_account");
        // userInfo에 넣기
        System.out.println(jsonMap);
        KakaoLoginDto kakaoUser = KakaoLoginDto.builder()
                .nickname(properties.get("nickname").toString())
                .profileImage(properties.get("profile_image").toString())
                .email(kakaoAccount.get("email").toString())
                .build();

        Optional<User> findUser = userRepository.findByEmail(kakaoUser.getEmail());
        if (findUser.isEmpty()) { // DB 에 없는 경우
            return kakaoUser.getEmail();
        }
        return jwtTokenUtil.generateToken(kakaoUser.getEmail());
    }
}
