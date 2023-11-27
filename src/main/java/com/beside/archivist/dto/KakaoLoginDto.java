package com.beside.archivist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
public class KakaoLoginDto {

    public String id;
    public String nickname;

    @Builder
    public KakaoLoginDto(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
