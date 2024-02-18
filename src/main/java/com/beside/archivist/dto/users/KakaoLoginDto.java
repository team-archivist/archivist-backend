package com.beside.archivist.dto.users;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoLoginDto {

    public Long userId;
    public String token;

    @Builder
    public KakaoLoginDto(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
