package com.beside.archivist.dto.home;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class HomeFeedDto {
    private List<Long> topList;
    private List<Long> middleList;
    private List<Long> bottomList;

    @Builder
    public HomeFeedDto(List<Long> topList, List<Long> middleList, List<Long> bottomList) {
        this.topList = topList;
        this.middleList = middleList;
        this.bottomList = bottomList;
    }
}
