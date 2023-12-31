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
    private String linkUrl;
    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String linkName;
    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String linkDesc;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_img_id")
    private LinkImg linkImg;

    @Builder
    public Link(String linkUrl, String linkName, String linkDesc, User user, LinkImg linkImg) {
        this.linkUrl = linkUrl;
        this.linkName = linkName;
        this.linkDesc = linkDesc;
        this.users = user;
        this.linkImg = linkImg;
    }
    public void update(LinkDto linkDto) {
        this.linkUrl = linkDto.getLinkUrl();
        this.linkName = linkDto.getLinkName();
        this.linkDesc = linkDto.getLinkDesc();
    }
}
