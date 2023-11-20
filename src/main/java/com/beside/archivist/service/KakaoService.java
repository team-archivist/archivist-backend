package com.beside.archivist.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Service
public interface KakaoService {
	
    public String getAccessTokenFromKakao(String client_id, String code) throws IOException;

    public HashMap<String, Object> getUserInfo(String access_Token) throws IOException;
}
