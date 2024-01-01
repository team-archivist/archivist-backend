package com.beside.archivist.dto.group;

import com.beside.archivist.entity.users.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class GroupDto {
    private Long groupId;
    @NotBlank(message = "그룹 이름은 공백일 수 없습니다.")
    @Size(min = 1, max = 100, message = "그룹 이름은 1자에서 100자 사이 여야 합니다.")
    private String groupName;           //그룹 이름
    @Size( max = 400, message = "그룹 설명은 400자 이내 이여야 합니다.")
    private String groupDesc;           //그룹 설명
    private Boolean isGroupPublic;      //그룹 공개 여부
    private List<Category> categories;  //그룹 카테고리
    private Long userId;
    private String imgUrl;
    private Long linkCount;

    @Builder
    public GroupDto(Long groupId, String groupName, String groupDesc, Boolean isGroupPublic, List<Category> categories, String imgUrl, Long userId,Long linkCount) {
        this.groupId = groupId;
        this.groupName = groupName.trim();
        this.groupDesc = groupDesc;
        this.isGroupPublic = isGroupPublic;
        this.categories = categories;
        this.imgUrl = imgUrl;
        this.userId = userId;
        this.linkCount = linkCount;
    }
}
