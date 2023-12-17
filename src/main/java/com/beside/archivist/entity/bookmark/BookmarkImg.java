package com.beside.archivist.entity.bookmark;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity @Table(name = "bookmark_img")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate // Dirty Checking
public class BookmarkImg {
    @Id @Column(name = "bookmark_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName; // 이미지 파일명
    private String oriImgName; // 원본 이미지 파일명
    private String imgUrl; // 이미지 조회 경로

    @OneToOne(mappedBy = "bookmarkImg", cascade = CascadeType.ALL, orphanRemoval = true)
    private Bookmark bookmark;

    @Builder
    public BookmarkImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
    public void updateBookmarkImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
