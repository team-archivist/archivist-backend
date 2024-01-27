package com.beside.archivist.dto.link;

import com.beside.archivist.entity.group.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class LinkInfoDto {
    private Long linkId;
    private String linkUrl;    //북마크 경로
    @NotBlank(message = "link 이름은 공백일 수 없습니다.")
    @Size(min = 1, max = 100, message = "link 이름은 1자에서 100자 사이 여야 합니다.")
    private String linkName;   //북마크 이름
    @Size( max = 400, message = "link 설명은 400자 이내 이여야 합니다.")
    private String linkDesc;   //북마크 설명
    private String imgUrl;
    private Long userId;

    private List<Group> groupList;

    @Builder
    public LinkInfoDto(Long linkId, String linkUrl, String linkName, String linkDesc, String imgUrl, Long userId,List<Group> groupList) {
        this.linkId = linkId;
        this.linkUrl = linkUrl;
        this.linkName = linkName.trim();
        this.linkDesc = linkDesc.trim();
        this.imgUrl = imgUrl;
        this.userId = userId;
        this.groupList = groupList;
    }
}
