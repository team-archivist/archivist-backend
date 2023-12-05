package com.beside.archivist.dto.bookmark;

import com.beside.archivist.entity.users.User;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class BookmarkDto {
    private String bookUrl;    //북마크 경로
    private String bookName;   //북마크 이름
    private String bookDesc;   //북마크 설명
    private User user;
}
