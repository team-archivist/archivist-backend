package com.beside.archivist.entity.link;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity @Table(name = "link_img")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate // Dirty Checking
public class LinkImg {
    @Id @Column(name = "link_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; // 이미지 파일명
    private String oriImgName; // 원본 이미지 파일명
    private String imgUrl; // 이미지 조회 경로

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    private Link link;

    @Builder
    public LinkImg(String imgName, String oriImgName, String imgUrl,Link link) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.link = link;
    }
    public void updateLinkImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
    public static LinkImg initializeDefaultLinkImg() {
        return LinkImg.builder()
                .imgName("linkDefaultImg")
                .oriImgName("linkDefaultImg.png")
                .imgUrl("/image/linkDefaultImg.png")
                .build();
    }

    public void saveLink(Link link){
        this.link = link;
    }
}
