package com.beside.archivist.entity.users;


import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.BaseTimeEntity;
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
@DynamicInsert @DynamicUpdate
@SQLDelete(sql = "UPDATE users SET is_deleted = 'Y', deleted_at = sysdate() WHERE user_id = ?")
@Where(clause = "is_deleted = 'N'")
public class User extends BaseEntity {


    public void deleteAndMaskEmail(String email) {
        if (email == null || email.length() < 3) {
            // 이메일이 null이거나 길이가 3 미만인 경우 처리하지 않음
            return;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            // '@' 기호가 없거나 처음에 나오면 처리하지 않음
            return;
        }

        String maskedPart = new String(new char[atIndex - 3]).replace('\0', '*');
        this.email = email.charAt(0) + maskedPart + email.substring(atIndex - 2);

        setIsDeleted("Y");
    }

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
    private List<UserGroup> userGroups = new ArrayList<>();

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
    public void addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
    }
}
