package com.beside.archivist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @PrePersist
    public void prePersist() {
        // 엔티티가 저장되기 전에 호출되는 메서드
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    public ZonedDateTime getCreatedAt() {
        return ZonedDateTime.ofInstant(createdAt, ZoneId.of( "Asia/Seoul" ));
    }

    public ZonedDateTime getUpdatedAt() {
        return ZonedDateTime.ofInstant(updatedAt, ZoneId.of( "Asia/Seoul" ));
    }

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}