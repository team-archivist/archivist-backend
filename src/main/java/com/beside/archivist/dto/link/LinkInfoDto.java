package com.beside.archivist.dto.link;

import com.beside.archivist.entity.group.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
public class LinkInfoDto {
    private Long linkId;
    private String linkUrl;    //북마크 경로
    private String linkName;   //북마크 이름
    private String linkDesc;   //북마크 설명
    private String imgUrl;
    private Long userId;
    private List<Long> groupList;

    @Builder
    public LinkInfoDto(Long linkId, String linkUrl, String linkName, String linkDesc, String imgUrl, Long userId,List<Long> groupList) {
        this.linkId = linkId;
        this.linkUrl = linkUrl;
        this.linkName = linkName.trim();
        this.linkDesc = linkDesc.trim();
        this.imgUrl = imgUrl;
        this.userId = userId;
        this.groupList = groupList;
    }
}
