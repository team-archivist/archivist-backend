package com.beside.archivist.service.home;

import com.beside.archivist.dto.home.HomeFeedDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.home.HomeFeed;
import com.beside.archivist.entity.link.Link;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.repository.group.GroupRepository;
import com.beside.archivist.repository.home.HomeFeedRepository;
import com.beside.archivist.repository.link.LinkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
class HomeFeedServiceTest {

    @Autowired
    LinkRepository linkRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    HomeFeedService homeFeedServiceImpl;
    @Autowired
    HomeFeedRepository homeFeedRepository;

    @AfterEach
    void tearDown() {
        homeFeedRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("상단 그룹 3개, 중간 그룹 4개, 하단 링크 5개로 홈 피드를 구성한다.")
    void saveHomeFeedTest() {
        // given
        List<Long> topList = createHomeFeedRequestList(HomeFeed.Section.TOP,3);
        List<Long> middleList = createHomeFeedRequestList(HomeFeed.Section.MIDDLE,4);
        List<Long> bottomList = createHomeFeedRequestList(HomeFeed.Section.BOTTOM,5);

        HomeFeedDto homeFeedDto = HomeFeedDto.builder()
                .topList(topList)
                .middleList(middleList)
                .bottomList(bottomList)
                .build();

        // when
        homeFeedServiceImpl.saveHomeFeed(homeFeedDto);

        // then
        List<HomeFeed> topFeeds = homeFeedRepository.findAll().stream()
                .filter(homeFeed -> homeFeed.getSection().equals(HomeFeed.Section.TOP))
                .toList();
        List<HomeFeed> middleFeeds = homeFeedRepository.findAll().stream()
                .filter(homeFeed -> homeFeed.getSection().equals(HomeFeed.Section.MIDDLE))
                .toList();
        List<HomeFeed> bottomFeeds = homeFeedRepository.findAll().stream()
                .filter(homeFeed -> homeFeed.getSection().equals(HomeFeed.Section.BOTTOM))
                .toList();

        assertThat(topFeeds).hasSize(3);
        assertThat(topFeeds.stream().map(HomeFeed::getObjectId).toList())
                .containsExactlyInAnyOrderElementsOf(topList);

        assertThat(middleFeeds).hasSize(4);
        assertThat(middleFeeds.stream().map(HomeFeed::getObjectId).toList())
                .containsExactlyInAnyOrderElementsOf(middleList);

        assertThat(bottomFeeds).hasSize(5);
        assertThat(bottomFeeds.stream().map(HomeFeed::getObjectId).toList())
                .containsExactlyInAnyOrderElementsOf(bottomList);
    }

    @Test
    @DisplayName("홈피드를 요청할 때마다 기존 홈피드 데이터는 초기화된다.")
    void getHomeFeedByLinkTest() {
        // given
        // 1. 3-4-4 로 저장
        List<Long> topList = createHomeFeedRequestList(HomeFeed.Section.TOP,3);
        List<Long> middleList = createHomeFeedRequestList(HomeFeed.Section.MIDDLE,4);
        List<Long> bottomList = createHomeFeedRequestList(HomeFeed.Section.BOTTOM,4);
        HomeFeedDto homeFeedDto = HomeFeedDto.builder()
                .topList(topList)
                .middleList(middleList)
                .bottomList(bottomList)
                .build();
        homeFeedServiceImpl.saveHomeFeed(homeFeedDto);
        // 2. 2-1-2 로 재 요청
        List<Long> newTopList = createHomeFeedRequestList(HomeFeed.Section.TOP,2);
        List<Long> newMiddleList = createHomeFeedRequestList(HomeFeed.Section.MIDDLE,1);
        List<Long> newBottomList = createHomeFeedRequestList(HomeFeed.Section.BOTTOM,2);
        HomeFeedDto updateHomeFeedDto = HomeFeedDto.builder()
                .topList(newTopList)
                .middleList(newMiddleList)
                .bottomList(newBottomList)
                .build();

        // when
        homeFeedServiceImpl.saveHomeFeed(updateHomeFeedDto);

        // then
        List<HomeFeed> topFeeds = homeFeedRepository.findAll().stream()
                .filter(homeFeed -> homeFeed.getSection().equals(HomeFeed.Section.TOP))
                .toList();
        List<HomeFeed> middleFeeds = homeFeedRepository.findAll().stream()
                .filter(homeFeed -> homeFeed.getSection().equals(HomeFeed.Section.MIDDLE))
                .toList();
        List<HomeFeed> bottomFeeds = homeFeedRepository.findAll().stream()
                .filter(homeFeed -> homeFeed.getSection().equals(HomeFeed.Section.BOTTOM))
                .toList();

        assertThat(topFeeds).hasSize(2);
        assertThat(topFeeds.stream().map(HomeFeed::getObjectId).toList())
                .containsExactlyInAnyOrderElementsOf(newTopList);

        assertThat(middleFeeds).hasSize(1);
        assertThat(middleFeeds.stream().map(HomeFeed::getObjectId).toList())
                .containsExactlyInAnyOrderElementsOf(newMiddleList);

        assertThat(bottomFeeds).hasSize(2);
        assertThat(bottomFeeds.stream().map(HomeFeed::getObjectId).toList())
                .containsExactlyInAnyOrderElementsOf(newBottomList);
    }

    public Link createLink(String linkUrl, String linkName, String linkDesc){
        return Link.builder()
                .linkUrl(linkUrl)
                .linkName(linkName)
                .linkDesc(linkDesc)
                .build();
    }
    public Group createGroup(String groupName, String groupDesc, List<Category> categories){
        return Group.builder()
                .groupName(groupName)
                .groupDesc(groupDesc)
                .categories(categories)
                .build();
    }
    List<Long> createHomeFeedRequestList(HomeFeed.Section section, int size){
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Long objectId = 0L;
            switch (section){
                case BOTTOM -> {
                    objectId = linkRepository.save(createLink("linkUrl" + i,
                            "linkName" + i, "linkDesc" + i)).getId();
                }
                case TOP,MIDDLE -> {
                    objectId = groupRepository.save(createGroup("groupName" + i,
                            "groupDesc" + i, List.of(Category.EXERCISE))).getId();
                }
            }
            idList.add(objectId);
        }
        return idList;
    }
}