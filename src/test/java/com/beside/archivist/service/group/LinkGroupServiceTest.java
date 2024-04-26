package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.link.Link;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.group.GroupNotFoundException;
import com.beside.archivist.exception.link.LinkNotFoundException;
import com.beside.archivist.repository.group.GroupRepository;
import com.beside.archivist.repository.link.LinkRepository;
import com.beside.archivist.repository.users.UserRepository;
import com.beside.archivist.service.link.LinkService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LinkGroupServiceTest {

    @Autowired
    private LinkGroupService linkGroupServiceImpl;
    @Autowired
    private LinkService linkServiceImpl;
    @Autowired
    private GroupService groupServiceImpl;
    @Autowired
    private GroupImgService groupImgServiceImpl;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        groupRepository.deleteAll();
        linkRepository.deleteAll();
        userRepository.deleteAll();
    }

    LinkDto createLinkDto(){
        return LinkDto.builder()
                .linkName("testLink")
                .linkDesc("testDesc")
                .linkUrl("www.test.com")
                .build();
    }

    GroupDto createGroupDto(String groupName, String groupDesc, String isGroupPublic, List<Category> categories) {
        return GroupDto.builder()
                .groupName(groupName)
                .groupDesc(groupDesc)
                .isGroupPublic(isGroupPublic)
                .categories(categories)
                .build();
    }

    User createUser(String email, String nickname, List<Category> categories){
        return userRepository.save(
                User.builder()
                        .email(email)
                        .nickname(nickname)
                        .categories(categories)
                        .build()
        );
    }


    @Test
    @DisplayName("그룹 내에 링크가 없는 상태에서, 링크 이미지를 추가하면 그룹 이미지가 업데이트 된다.")
    void changeGroupImgToLinkImgTest() {
        // given
        User savedUser = createUser("limnj@test.com", "limnj", List.of(Category.LIFESTYLE));
        // 1. 이미지 없이 그룹 생성
        GroupDto groupDto = createGroupDto("group1","group1 description",
                "Y", List.of(Category.LIFESTYLE,Category.KNOWLEDGE));
        GroupDto savedGroupDto = groupServiceImpl.saveGroup(groupDto, null);
        // 2. 링크 생성 + 그룹에 링크 저장
        MockMultipartFile linkImg = new MockMultipartFile(
                "봄이다",
                "spring.jpg",
                String.valueOf(MediaType.IMAGE_JPEG),
                "Spring!".getBytes()
        );
        LinkDto savedLinkDto = linkServiceImpl.saveLink(createLinkDto(),
                new Long[]{savedGroupDto.getGroupId()}, savedUser.getEmail(),linkImg);

        // when
        linkGroupServiceImpl.changeGroupImg(savedGroupDto.getGroupId());

        // then
        Link findLink = linkRepository.findById(savedLinkDto.getLinkId()).orElseThrow(
                () -> new LinkNotFoundException(ExceptionCode.LINK_NOT_FOUND));
        Group findGroup = groupRepository.findById(savedGroupDto.getGroupId()).orElseThrow(
                () -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND)
        );

        assertEquals(findLink.getLinkImg().getImgName(),findGroup.getGroupImg().getImgName());
        assertEquals(findLink.getLinkImg().getOriImgName(),findGroup.getGroupImg().getOriImgName());
        assertEquals(findLink.getLinkImg().getImgUrl(),findGroup.getGroupImg().getImgUrl());
    }

    @Test
    @DisplayName("그룹에 링크 이미지가 저장되어 있을 때 그룹 이미지를 변경하면 변경한 이미지가 저장된다.")
    void updateLinkImgTest() {
        // given
        User savedUser = createUser("limnj@test.com", "limnj", List.of(Category.LIFESTYLE));
        // 1. 이미지 없이 그룹 생성
        GroupDto groupDto = createGroupDto("group1","group1 description",
                "Y", List.of(Category.LIFESTYLE,Category.KNOWLEDGE));
        GroupDto savedGroupDto = groupServiceImpl.saveGroup(groupDto, null);
        // 2. 링크 생성
        MockMultipartFile linkImg = new MockMultipartFile(
                "봄이다",
                "spring.jpg",
                String.valueOf(MediaType.IMAGE_JPEG),
                "Spring!".getBytes()
        );
        linkServiceImpl.saveLink(createLinkDto(), new Long[]{savedGroupDto.getGroupId()}, savedUser.getEmail(), linkImg);
        // 3. 그룹 이미지 업데이트 ( 링크 이미지로 변경 )
        linkGroupServiceImpl.changeGroupImg(savedGroupDto.getGroupId());
        // 4. 그룹 이미지 업데이트 ( 그룹 이미지로 변경 )
        Group savedGroup = groupRepository.findById(savedGroupDto.getGroupId()).orElseThrow(
                () -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND)
        );
        MockMultipartFile groupImg = new MockMultipartFile(
                "여름이다",
                "summer.jpg",
                String.valueOf(MediaType.IMAGE_JPEG),
                "summer!".getBytes()
        );

        // when
        groupImgServiceImpl.changeGroupImg(savedGroup.getGroupImg().getId(), groupImg);

        // then
        Group findGroup = groupRepository.findById(savedGroup.getId()).orElseThrow(
                () -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND)
        );
        assertEquals(groupImg.getOriginalFilename(),findGroup.getGroupImg().getOriImgName());

    }

}