package com.beside.archivist.dto.link;

import com.beside.archivist.entity.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class LinkDto {
    private Long linkId;
    private String bookUrl;    //북마크 경로
    private String bookName;   //북마크 이름
    private String bookDesc;   //북마크 설명
    private String imgUrl;
    private Long userId;

    @Builder
    public LinkDto(Long linkId, String bookUrl, String bookName, String bookDesc, String imgUrl,Long userId) {
        this.linkId = linkId;
        this.bookUrl = bookUrl;
        this.bookName = bookName;
        this.bookDesc = bookDesc;
        this.imgUrl = imgUrl;
        this.userId = userId;
    }
}
