package com.beside.archivist.service.home;

import com.beside.archivist.dto.home.HomeFeedDto;
import com.beside.archivist.dto.home.HomeFeedInfoDto;
import com.beside.archivist.entity.home.HomeFeed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomeFeedService {
    void saveHomeFeed(HomeFeedDto homeFeedDto);
    List<HomeFeedInfoDto<?>> getHomeFeedByType(HomeFeed.Type type);
}
