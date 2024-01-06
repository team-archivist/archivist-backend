package com.beside.archivist.entity.users;


import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.BaseTimeEntity;
import com.beside.archivist.entity.bookmark.Bookmark;
import com.beside.archivist.entity.link.Link;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "users") // user 예약어라 users로 명명
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> links = new ArrayList<>();

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

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

    public void addLink(Link l){
        this.links.add(l);
    }
    public void addBookmark(Bookmark b) {
        this.bookmarks.add(b);
    }
}
