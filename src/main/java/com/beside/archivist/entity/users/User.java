package com.beside.archivist.entity.users;


import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.usergroup.UserGroup;
import com.beside.archivist.entity.link.Link;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "users") // user 예약어라 users로 명명
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET is_deleted = 'Y', deleted_at = sysdate() WHERE user_id = ?")
@Where(clause = "is_deleted = 'N'")
public class User extends BaseEntity {

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

    @OneToOne(mappedBy = "users",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private UserImg userImg;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> links = new ArrayList<>();

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroup> userGroups = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname, List<Category> categories) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.categories = categories;
        this.provider = "kakao";
    }

    public void updateUserInfo(String nickname, List<Category> categories) {
        this.nickname = nickname;
        this.categories = categories;
    }

    public void updateUserEmail(String email) {
        this.email = email;
    }

    public void addLink(Link l){
        this.links.add(l);
    }
    public void addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
    }

    public void saveUserImg(UserImg userImg) {
        this.userImg = userImg;
    }
}
