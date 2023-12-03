package com.beside.archivist.entity.link;


import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.link.Link;
import com.beside.archivist.entity.users.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Table(name = "link")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link {

    @Id @Column(name = "link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;
    @Column
    private String email;
    @Column
    private String nickname;
    @Column
    private List<Category> categories;

    @Builder
    public Link(String email, String nickname, List<Category> categories,User user) {
        this.email = email;
        this.nickname = nickname;
        this.categories = categories;
        this.user = user;
    }
    public void update(LinkDto linkDto) {
        this.nickname = linkDto.getNickname();
        this.categories = linkDto.getCategories();
    }
}
