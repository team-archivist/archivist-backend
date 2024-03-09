package com.beside.archivist.entity.redis;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "users", timeToLive = 60)
public class Users {

    @Id
    private Long id;

    private String email; // 변경 불가

    private String nickname; // 변경 가능
    private String password; // 임의 지정


    public Users(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}