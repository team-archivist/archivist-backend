package com.beside.archivist.entity.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.bookmark.Bookmark;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "group_info")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseEntity {

    @Id @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String groupName;
    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String groupDesc;
    @Column
    @ColumnDefault("0") //default 0
    private boolean isGroupPublic;
    @Column
    private List<Category> categories;
    @Formula("(SELECT COUNT(lg.link_group_id) FROM link_group lg WHERE lg.group_id = group_id)")
    private Long linkCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_img_id")
    private GroupImg groupImg;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<LinkGroup> links;

    @OneToMany(mappedBy = "groups", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();


    @Builder
    public Group(String groupName, String groupDesc, Boolean isGroupPublic, List<Category> categories,GroupImg groupImg, Long linkCount, List<LinkGroup> links) {
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.isGroupPublic = isGroupPublic;
        this.categories = categories;
        this.groupImg = groupImg;
        this.linkCount = linkCount;
        this.links = links;
    }
    public void update(GroupDto groupDto) {
        this.groupName = groupDto.getGroupName();
        this.groupDesc = groupDto.getGroupDesc();
        this.isGroupPublic = groupDto.getIsGroupPublic();
        this.categories = groupDto.getCategories();
        this.linkCount = groupDto.getLinkCount();
    }

    public void addBookmark(Bookmark b) {
        this.bookmarks.add(b);
    }
}
