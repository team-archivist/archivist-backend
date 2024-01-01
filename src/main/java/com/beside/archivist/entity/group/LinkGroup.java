package com.beside.archivist.entity.group;

import com.beside.archivist.entity.BaseEntity;
import com.beside.archivist.entity.link.Link;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "link_group")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkGroup extends BaseEntity {

    @Id @Column(name = "link_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    private Link link;

    @Builder
    public LinkGroup(Group group, Link link) {
        this.group = group;
        this.link = link;
    }

}
