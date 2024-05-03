package com.beside.archivist.dto.link;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LinkDto {
    private Long linkId;
    private String linkUrl;    //북마크 경로
    @NotBlank(message = "link 이름은 공백일 수 없습니다.")
    @Size(min = 1, max = 100, message = "link 이름은 1자에서 100자 사이 여야 합니다.")
    private String linkName;   //북마크 이름
    @Size( max = 400, message = "link 설명은 400자 이내 이여야 합니다.")
    private String linkDesc;   //북마크 설명

    @Builder
    public LinkDto(Long linkId, String linkUrl, String linkName, String linkDesc) {
        this.linkId = linkId;
        this.linkUrl = linkUrl;
        this.linkName = linkName.trim();
        this.linkDesc = linkDesc.trim();
    }
}
