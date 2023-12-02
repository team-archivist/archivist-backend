package com.beside.archivist.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
public class KakaoLoginDto {

    public String id;
    public String nickname;
    public String profileImage;

    @Builder
    public KakaoLoginDto(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
