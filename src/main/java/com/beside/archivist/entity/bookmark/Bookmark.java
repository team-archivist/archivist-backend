package com.beside.archivist.entity.bookmark;


import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.users.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "bookmark")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @Id @Column(name = "bookmark_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User users;
    @Column
    private String bookUrl;
    @Column
    private String bookName;
    @Column
    private String bookDesc;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_img_id")
    private BookmarkImg bookmarkImg;

    @Builder
    public Bookmark(String bookUrl, String bookName, String bookDesc, User user, BookmarkImg bookmarkImg) {
        this.bookUrl = bookUrl;
        this.bookName = bookName;
        this.bookDesc = bookDesc;
        this.users = user;
        this.bookmarkImg = bookmarkImg;
    }
    public void update(BookmarkDto bookmarkDto) {
        this.bookUrl = bookmarkDto.getBookUrl();
        this.bookName = bookmarkDto.getBookName();
        this.bookDesc = bookmarkDto.getBookDesc();
    }
}
