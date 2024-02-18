package com.beside.archivist.entity.users;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity @Table(name = "user_img")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate // Dirty Checking
public class UserImg {
    @Id @Column(name = "user_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; // 이미지 파일명
    private String oriImgName; // 원본 이미지 파일명
    private String imgUrl; // 이미지 조회 경로

    @OneToOne(mappedBy = "userImg", cascade = CascadeType.ALL, orphanRemoval = true)
    private User users;

    @Builder
    public UserImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
    public void updateUserImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
