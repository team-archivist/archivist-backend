package com.beside.archivist.dto.bookmark;

import com.beside.archivist.entity.users.User;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class BookmarkDto {
    private String book_url;    //북마크 경로
    private String book_name;   //북마크 이름
    private String book_desc;   //북마크 설명
    private User user;
}
