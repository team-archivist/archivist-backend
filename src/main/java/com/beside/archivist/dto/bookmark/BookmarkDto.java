package com.beside.archivist.dto.bookmark;

import com.beside.archivist.entity.bookmark.BookmarkImg;
import com.beside.archivist.entity.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class BookmarkDto {
    private Long bookmarkId;
    private String bookUrl;    //북마크 경로
    private String bookName;   //북마크 이름
    private String bookDesc;   //북마크 설명
    private String imgUrl;
    private User user;

    @Builder
    public BookmarkDto(Long bookmarkId, String bookUrl, String bookName, String bookDesc, String imgUrl) {
        this.bookmarkId = bookmarkId;
        this.bookUrl = bookUrl;
        this.bookName = bookName;
        this.bookDesc = bookDesc;
        this.imgUrl = imgUrl;
    }
}
