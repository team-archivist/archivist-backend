package com.beside.archivist.dto.group;

import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.users.Category;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
public class GroupDto {
    @NotBlank(message = "그룹 이름은 공백일 수 없습니다.")
    @Size(min = 1, max = 100, message = "그룹 이름은 1자에서 100자 사이 여야 합니다.")
    private String groupName;           //그룹 이름
    @Size( max = 400, message = "그룹 설명은 400자 이내 이여야 합니다.")
    private String groupDesc;           //그룹 설명
    private String isGroupPublic;      //그룹 공개 여부
    private List<Category> categories;  //그룹 카테고리

    @Builder
    public GroupDto( String groupName, String groupDesc, String isGroupPublic, List<Category> categories) {
        this.groupName = groupName != null ? groupName.trim() : groupName;
        this.groupDesc = groupDesc;
        this.isGroupPublic = isGroupPublic;
        this.categories = categories;
    }

    @QueryProjection
    public GroupDto(Group group) {
        this.groupName = group.getGroupName().trim();
        this.groupDesc = group.getGroupDesc();
        this.isGroupPublic = group.getIsGroupPublic();
        this.categories = group.getCategories();
    }
}
