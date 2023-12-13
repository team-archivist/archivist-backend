package com.beside.archivist.entity.users;


import com.beside.archivist.dto.users.UserDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Table(name = "users") // user 예약어라 users로 명명
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email; // 변경 불가
    @Column
    private String nickname; // 변경 가능
    @Column
    private String password; // 임의 지정
    @Column
    private List<Category> categories; // 변경 가능
    @Column
    private String provider; // kakao or admin

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_img_id")
    private UserImg userImg;

    @Builder
    public User(String email, String password, String nickname, List<Category> categories, UserImg userImg) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.categories = categories;
        this.provider = "kakao";
        this.userImg = userImg;
    }

    public void updateUserInfo(String nickname, List<Category> categories) {
        this.nickname = nickname;
        this.categories = categories;
    }
}
