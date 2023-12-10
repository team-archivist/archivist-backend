package com.beside.archivist.entity.users;


import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Table(name = "users") // user 예약어라 users로 명명
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private String nickname;
    @Column
    private String password;
    @Column
    private List<Category> categories;
    @Column
    private String provider;

    @Builder
    public User(String email, String password, String nickname, List<Category> categories) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.categories = categories;
    }
    public void update(UserDto userDto) {
        this.nickname = userDto.getNickname();
        this.categories = userDto.getCategories();
    }
}
