package com.beside.archivist.entity.link;


import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.users.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "link")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link extends BaseEntity {

    @Id @Column(name = "link_id")
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
    @JoinColumn(name = "link_img_id")
    private LinkImg linkImg;

    @Builder
    public Link(String bookUrl, String bookName, String bookDesc, User user, LinkImg linkImg) {
        this.bookUrl = bookUrl;
        this.bookName = bookName;
        this.bookDesc = bookDesc;
        this.users = user;
        this.linkImg = linkImg;
    }
    public void update(LinkDto linkDto) {
        this.bookUrl = linkDto.getBookUrl();
        this.bookName = linkDto.getBookName();
        this.bookDesc = linkDto.getBookDesc();
    }
}