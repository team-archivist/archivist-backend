package com.beside.archivist.entity.usergroup;

import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.users.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table @Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup {
    @Id
    @Column(name = "user_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isOwner;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User users;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group groups;

    @Builder
    public UserGroup(boolean isOwner, User users, Group groups) {
        this.isOwner = isOwner;
        this.users = users;
        this.groups = groups;
    }
}
