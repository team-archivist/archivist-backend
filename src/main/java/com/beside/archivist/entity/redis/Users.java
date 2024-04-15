package com.beside.archivist.entity.redis;

import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.UserImg;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@RedisHash(value = "users", timeToLive = 60)
public class Users {

    @Id
    private String email; // 변경 불가

    private String nickname; // 변경 가능
    private String password; // 임의 지정
    List<Category> categories;
    UserImg userImg;


    public Users(String email, String nickname, String password, List<Category> categories, UserImg userImg) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.categories = categories;
        this.userImg = userImg;
    }
}