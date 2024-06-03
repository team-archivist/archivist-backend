package com.beside.archivist.dto.group;

import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.users.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupInfoDto {
    private Long groupId;
    private String groupName;           //그룹 이름
    private String groupDesc;           //그룹 설명
    private String isGroupPublic;      //그룹 공개 여부
    private List<Category> categories;  //그룹 카테고리
    private String imgUrl;
    private Long linkCount;
    private String isDefault;

    @Builder
    public GroupInfoDto(Long groupId, String groupName, String groupDesc, String isGroupPublic, List<Category> categories, String imgUrl, Long linkCount, String isDefault) {
        this.groupId = groupId;
        this.groupName = groupName != null ? groupName.trim() : groupName;
        this.groupDesc = groupDesc;
        this.isGroupPublic = isGroupPublic;
        this.categories = categories;
        this.imgUrl = imgUrl;
        this.linkCount = linkCount;
        this.isDefault = isDefault;
    }

    @QueryProjection
    public GroupInfoDto(Group group) {
        this.groupId = group.getId();
        this.groupName = group.getGroupName().trim();
        this.groupDesc = group.getGroupDesc();
        this.isGroupPublic = group.getIsGroupPublic();
        this.categories = group.getCategories();
        this.imgUrl = group.getGroupImg().getImgUrl();
        this.linkCount = group.getLinkCount();
        this.isDefault = group.getIsDefault();
    }
}
