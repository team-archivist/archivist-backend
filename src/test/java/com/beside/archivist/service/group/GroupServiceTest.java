package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.group.GroupInfoDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.exception.group.GroupNotFoundException;
import com.beside.archivist.repository.group.GroupImgRepository;
import com.beside.archivist.repository.group.GroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GroupServiceTest {

    @Autowired
    private GroupService groupServiceImpl;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupImgRepository groupImgRepository;

    @AfterEach
    void tearDown() {
        groupRepository.deleteAll();
        groupImgRepository.deleteAll();
    }

    GroupDto createGroupDto(String groupName, String groupDesc, String isGroupPublic, List<Category> categories) {
        return GroupDto.builder()
                .groupName(groupName)
                .groupDesc(groupDesc)
                .isGroupPublic(isGroupPublic)
                .categories(categories)
                .build();
    }

    @Test
    @DisplayName("그룹을 생성한다")
    void createGroupTest() {
        // given
        GroupDto groupDto = createGroupDto("group1","group1 description","Y",
                List.of(Category.LIFESTYLE,Category.KNOWLEDGE));

        // when
        GroupInfoDto savedGroupDto = groupServiceImpl.saveGroup(groupDto, null);

        // then
        Group savedGroup = groupRepository.findById(savedGroupDto.getGroupId()).orElseThrow();
        assertAll("savedGroup",
                () -> assertEquals(groupDto.getGroupName(),savedGroup.getGroupName()),
                () -> assertEquals(groupDto.getGroupDesc(),savedGroup.getGroupDesc()),
                () -> assertEquals(groupDto.getIsGroupPublic(),savedGroup.getIsGroupPublic()),
                () -> assertEquals(groupDto.getCategories(),savedGroup.getCategories())
        );
    }

    @Test
    @DisplayName("특정 그룹을 조회한다.")
    void findByGroupIdTest() {
        // given
        GroupDto groupDto = createGroupDto("group1","group1 description","Y",
                List.of(Category.LIFESTYLE,Category.KNOWLEDGE));
        GroupInfoDto savedGroupDto = groupServiceImpl.saveGroup(groupDto, null);

        // when
        GroupInfoDto findGroup = groupServiceImpl.findGroupById(savedGroupDto.getGroupId());

        // then
        assertAll("findGroup",
                () -> assertEquals(groupDto.getGroupName(),findGroup.getGroupName()),
                () -> assertEquals(groupDto.getGroupDesc(),findGroup.getGroupDesc()),
                () -> assertEquals(groupDto.getIsGroupPublic(),findGroup.getIsGroupPublic()),
                () -> assertEquals(groupDto.getCategories(),findGroup.getCategories())
        );
    }

    @Test
    @DisplayName("그룹의 이름과 설명, 카테고리를 수정한다.")
    void updateGroupTest() {
        //given
        GroupDto groupDto = createGroupDto("group1","group1 description",
                "Y", List.of(Category.LIFESTYLE,Category.KNOWLEDGE));
        GroupInfoDto savedGroupDto = groupServiceImpl.saveGroup(groupDto, null);
        GroupDto updateRequest = createGroupDto("updatedGroup", "Group is Updated",
                "Y", List.of(Category.EXERCISE));

        // when
        groupServiceImpl.updateGroup(savedGroupDto.getGroupId(), updateRequest, null);

        // then
        Group updatedGroup = groupRepository.findById(savedGroupDto.getGroupId()).orElseThrow();
        assertAll("updatedGroup",
                () -> assertEquals(updateRequest.getGroupName(),updatedGroup.getGroupName()),
                () -> assertEquals(updateRequest.getGroupDesc(),updatedGroup.getGroupDesc()),
                () -> assertEquals(updateRequest.getIsGroupPublic(),updatedGroup.getIsGroupPublic()),
                () -> assertEquals(updateRequest.getCategories(),updatedGroup.getCategories())
        );
    }

    @Test
    @DisplayName("그룹을 삭제한다.")
    void deleteGroupTest() {
        // given
        GroupDto groupDto = createGroupDto("group1","group1 description",
                "Y", List.of(Category.LIFESTYLE,Category.KNOWLEDGE));
        GroupInfoDto savedGroupDto = groupServiceImpl.saveGroup(groupDto, null);

        // when
        groupServiceImpl.deleteGroup(savedGroupDto.getGroupId());

        // then
        assertThat(groupRepository.findAll()).hasSize(0);
        assertThat(groupImgRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("찾는 그룹이 없을 때는 예외가 발생한다.")
    void groupNotFoundExceptionTest() {
        // given
        Long groupId = 1L;

        // when //then
        assertThatThrownBy(() -> groupServiceImpl.findGroupById(groupId))
                .isInstanceOf(GroupNotFoundException.class);
    }

}