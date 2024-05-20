package com.beside.archivist.service.home;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.group.GroupInfoDto;
import com.beside.archivist.dto.home.HomeFeedDto;
import com.beside.archivist.dto.home.HomeFeedInfoDto;
import com.beside.archivist.dto.link.LinkInfoDto;
import com.beside.archivist.entity.home.HomeFeed;
import com.beside.archivist.repository.home.HomeFeedRepository;
import com.beside.archivist.service.group.GroupService;
import com.beside.archivist.service.link.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional
@RequiredArgsConstructor
public class HomeFeedServiceImpl implements HomeFeedService{
    private final HomeFeedRepository homeFeedRepository;
    private final GroupService groupServiceImpl;
    private final LinkService linkServiceImpl;

    /**
     * 홈피드 데이터 저장
     * @param homeFeedDto
     */
    @Override
    public void saveHomeFeed(HomeFeedDto homeFeedDto) {
        homeFeedRepository.deleteAllInBatch(); // 데이터 저장 전 전체 삭제
        saveHomeFeedBySection(homeFeedDto.getTopList(), HomeFeed.Section.TOP, HomeFeed.Type.GROUP);
        saveHomeFeedBySection(homeFeedDto.getMiddleList(), HomeFeed.Section.MIDDLE, HomeFeed.Type.GROUP);
        saveHomeFeedBySection(homeFeedDto.getBottomList(), HomeFeed.Section.BOTTOM, HomeFeed.Type.LINK);
    }

    /**
     * 타입 별(GROUP/LINK)로 홈피드 데이터 응답
     * @param type
     * @return
     */
    @Override
    public List<HomeFeedInfoDto<?>> getHomeFeedByType(HomeFeed.Type type) {
        List<HomeFeed> homeFeedList = homeFeedRepository.findByType(type);

        return homeFeedList.stream()
                .map(
                        homeFeed -> HomeFeedInfoDto.builder()
                            .section(homeFeed.getSection().toString())
                            .data(fetchData(homeFeed))
                            .build())
                .collect(Collectors.toList());
    }

    /**
     * 섹션/타입 별로 HomeFeed 데이터 저장
     * @param objectIds
     * @param section
     * @param type
     */
    private void saveHomeFeedBySection(List<Long> objectIds, HomeFeed.Section section, HomeFeed.Type type) {
        objectIds.forEach(
                id -> homeFeedRepository.save(HomeFeed.builder()
                        .objectId(id)
                        .type(type)
                        .section(section)
                        .build())
        );
    }

    /**
     * 타입에 맞는 Object(GROUP/LINK) 반환
     * @param homeFeed
     * @return
     */
    private Object fetchData(HomeFeed homeFeed) {
        return switch (homeFeed.getType()){
            case GROUP -> groupServiceImpl.findGroupById(homeFeed.getObjectId());
            case LINK -> linkServiceImpl.findLinkById(homeFeed.getObjectId());
        };
    }
}
