package com.beside.archivist.entity.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.usergroup.UserGroup;
import com.beside.archivist.entity.users.Category;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "group_info")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE group_info SET is_deleted = 'Y', deleted_at = sysdate() WHERE group_id = ?")
@Where(clause = "is_deleted = 'N'")
public class Group extends BaseEntity {

    @Id @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String groupName;
    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String groupDesc;
    @Column
    private String isGroupPublic; // default: 'Y'
    @Column
    private List<Category> categories;
    @Column
    private String isDefault;
    @Formula("(SELECT COUNT(lg.link_group_id) FROM link_group lg WHERE lg.group_id = group_id)")
    private Long linkCount;

    @OneToOne(mappedBy = "group",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private GroupImg groupImg;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<LinkGroup> linkGroups = new ArrayList<>();

    @OneToMany(mappedBy = "groups", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroup> userGroups = new ArrayList<>();


    @Builder
    public Group(String groupName, String groupDesc, String isGroupPublic, List<Category> categories, Long linkCount, List<LinkGroup> linkGroups, String isDefault) {
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.isGroupPublic = isGroupPublic == null ? "Y" : isGroupPublic;
        this.categories = categories;
        this.linkCount = linkCount;
        this.linkGroups = linkGroups;
        this.isDefault = isDefault == null ? "N" : isDefault;
    }
    public void update(GroupDto groupDto) {
        this.groupName = groupDto.getGroupName();
        this.groupDesc = groupDto.getGroupDesc();
        this.isGroupPublic = groupDto.getIsGroupPublic();
        this.categories = groupDto.getCategories();
    }

    public void addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
    }
    public void addLinkGroup(LinkGroup linkGroup) {
        this.linkGroups.add(linkGroup);
    }
    public void saveGroupImg(GroupImg groupImg){
        this.groupImg = groupImg;
    }

    public static Group fetchDefaultGroup() { // 기본 그룹 비공개
        return Group.builder()
                .groupName("기본 그룹")
                .groupDesc("기본 그룹")
                .isDefault("Y")
                .isGroupPublic("N")
                .build();
    }
}
