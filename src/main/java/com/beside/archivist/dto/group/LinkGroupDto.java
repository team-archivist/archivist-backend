package com.beside.archivist.dto.group;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class LinkGroupDto {

    private Long linkGroupId;
    private Long groupId;
    private Long linkId;

    @Builder
    public LinkGroupDto(Long linkGroupId, Long groupId, Long linkId) {
        this.linkGroupId = linkGroupId;
        this.groupId = groupId;
        this.linkId = linkId;
    }
}