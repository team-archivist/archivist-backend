package com.beside.archivist.entity.home;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table @Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HomeFeed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long objectId;
    @Enumerated(value = EnumType.STRING)
    private Type type;
    @Enumerated(value = EnumType.STRING)
    private Section section;

    public enum Type {
        GROUP, LINK
    }

    public enum Section {
        TOP, MIDDLE, BOTTOM
    }

    @Builder
    public HomeFeed(Long objectId, Type type, Section section) {
        this.objectId = objectId;
        this.type = type;
        this.section = section;
    }
}
