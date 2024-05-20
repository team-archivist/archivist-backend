package com.beside.archivist.dto.home;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HomeFeedInfoDto<T> {

    private String section;
    private T data;

    @Builder
    public HomeFeedInfoDto(String section, T data) {
        this.section = section;
        this.data = data;
    }
}
