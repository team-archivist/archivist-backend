package com.beside.archivist.entity.group;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity @Table(name = "group_img")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate // Dirty Checking
public class GroupImg {
    @Id @Column(name = "group_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; // 이미지 파일명
    private String oriImgName; // 원본 이미지 파일명
    private String imgUrl; // 이미지 조회 경로

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id") // group_id가 외래키로 사용됨
    private Group group;

    @Builder
    public GroupImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
    public void updateGroupImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
    public static GroupImg initializeDefaultLinkImg() {
        return GroupImg.builder()
                .imgName("linkDefaultImg")
                .oriImgName("linkDefaultImg.png")
                .imgUrl("/image/linkDefaultImg.png")
                .build();
    }
}
