package com.beside.archivist.entity.bookmark;


import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.users.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "bookmark")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id @Column(name = "bookmark_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;
    @Column
    private String book_url;
    @Column
    private String book_name;
    @Column
    private String book_desc;

    @Builder
    public Bookmark(String book_url, String book_name, String book_desc, User user) {
        this.book_url = book_url;
        this.book_name = book_name;
        this.book_desc = book_desc;
        this.user = user;
    }
    public void update(BookmarkDto bookmarkDto) {
        this.book_url = bookmarkDto.getBook_url();
        this.book_name = bookmarkDto.getBook_name();
        this.book_desc = bookmarkDto.getBook_desc();
    }
}
