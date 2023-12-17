package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.KakaoLoginDto;

import org.springframework.stereotype.Service;

@Service
public interface KakaoService {
	
    String getAccessTokenFromKakao(String code);

    String getUserInfo(String accessToken);
}
