package com.beside.archivist.dto.users;

import com.beside.archivist.entity.users.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public class UserInfoDto {

    private Long userId;
    private String email; // 변경 불가
    private String nickname; // 변경 가능
    private List<Category> categories; // 변경 가능
    private String imgUrl;

    @Builder
    public UserInfoDto(Long userId, String email, String nickname, List<Category> categories, String imgUrl) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.categories = categories;
        this.imgUrl = imgUrl;
    }

}
