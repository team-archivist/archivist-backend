package com.beside.archivist.service;

import com.beside.archivist.dto.KakaoLoginDto;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public interface KakaoService {
	
    String getAccessTokenFromKakao(String code);

    KakaoLoginDto getUserInfo(String accessToken);
}
