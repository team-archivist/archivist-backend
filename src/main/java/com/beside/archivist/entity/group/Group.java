package com.beside.archivist.entity.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User users;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_img_id")
    private GroupImg groupImg;


    @Builder
    public Group(String groupName, String groupDesc, Boolean isGroupPublic, User user, List<Category> categories,GroupImg groupImg) {
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.isGroupPublic = isGroupPublic;
        this.users = user;
        this.categories = categories;
        this.groupImg = groupImg;
    }
    public void update(GroupDto groupDto) {
        this.groupName = groupDto.getGroupName();
        this.groupDesc = groupDto.getGroupDesc();
        this.isGroupPublic = groupDto.getIsGroupPublic();
        this.categories = groupDto.getCategories();
    }
}
