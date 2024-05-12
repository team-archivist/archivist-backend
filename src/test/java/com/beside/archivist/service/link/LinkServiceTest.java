package com.beside.archivist.service.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.dto.link.LinkInfoDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.link.Link;
import com.beside.archivist.entity.link.LinkImg;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.repository.group.GroupRepository;
import com.beside.archivist.repository.link.LinkImgRepository;
import com.beside.archivist.repository.link.LinkRepository;
import com.beside.archivist.repository.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LinkServiceTest {

    @Autowired
    private LinkService linkServiceImpl;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private LinkImgRepository linkImgRepository;
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

    public User createUser(String email, String nickname, List<Category> categories){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .categories(categories)
                .build();
    }

    public Group createGroup(String groupName, String groupDesc, List<Category> categories){
        return Group.builder()
                .groupName(groupName)
                .groupDesc(groupDesc)
                .categories(categories)
                .build();
    }

    private LinkDto createLinkDto(String linkName, String linkUrl, String linkDesc) {
        return LinkDto.builder()
                .linkName(linkName)
                .linkUrl(linkUrl)
                .linkDesc(linkDesc)
                .build();
    }

    private Link createLinkEntity(String linkName, String linkUrl, String linkDesc) {
        return Link.builder()
                .linkName(linkName)
                .linkUrl(linkUrl)
                .linkDesc(linkDesc)
                .build();
    }

    @Test
    @DisplayName("링크 이미지 없이 링크를 저장한다")
    void saveLinkTest() {
        // given
        User user = createUser("limnj@test.com","limnj",List.of(Category.CULTURE));
        User savedUser = userRepository.save(user);

        Group group = createGroup("자주찾음","자주 찾는 사이트 모음", List.of(Category.EXERCISE));
        Group savedGroup = groupRepository.save(group);

        LinkDto linkDto = createLinkDto("깃허브 홈페이지","github.com","깃허브 홈페이지 설명");

        // when
        LinkInfoDto savedLink =
                linkServiceImpl.saveLink(linkDto, new Long[]{savedGroup.getId()}, savedUser.getEmail(), null);

        // then
        assertEquals(user.getId(), savedLink.getUserId());
        assertAll("savedLink",
                () -> assertEquals(savedLink.getLinkName(),linkDto.getLinkName()),
                () -> assertEquals(savedLink.getLinkDesc(),linkDto.getLinkDesc()),
                () -> assertEquals(savedLink.getLinkUrl(),linkDto.getLinkUrl()),
                () -> assertEquals(savedLink.getImgUrl(),"/image/linkDefaultImg.png") // 초기 디폴트 이미지
        );
    }

    @Test
    @DisplayName("저장한 특정 링크를 조회한다.")
    void findLinkByIdTest() {
        // given
        Link link = createLinkEntity("깃허브 홈페이지","github.com","깃허브 홈페이지 설명");
        Link savedLink = linkRepository.save(link);

        LinkImg linkImg = LinkImg.initializeDefaultLinkImg();
        savedLink.saveLinkImg(linkImg);
        linkImg.saveLink(savedLink);
        linkImgRepository.save(linkImg);

        // when
        LinkInfoDto findLink = linkServiceImpl.findLinkById(savedLink.getId());

        // then
        assertAll("findLink",
                () -> assertEquals(findLink.getLinkName(),link.getLinkName()),
                () -> assertEquals(findLink.getLinkDesc(),link.getLinkDesc()),
                () -> assertEquals(findLink.getLinkUrl(),link.getLinkUrl()),
                () -> assertEquals(findLink.getImgUrl(),link.getLinkImg().getImgUrl())
        );

    }

    @Test
    @DisplayName("링크 이름, URL, 설명을 수정한다.")
    void updateLinkTest() {
        // given
        Group group = createGroup("자주찾음","자주 찾는 사이트 모음", List.of(Category.EXERCISE));
        Group savedGroup = groupRepository.save(group);

        Link link = createLinkEntity("깃허브 홈페이지","github.com","깃허브 홈페이지 설명");
        Link savedLink = linkRepository.save(link);

        LinkImg linkImg = LinkImg.initializeDefaultLinkImg();
        linkImg.saveLink(savedLink);
        linkImgRepository.save(linkImg);

        LinkDto updateLinkDto =  createLinkDto("우리 홈페이지","arcave.com","우리 홈페이지 설명");

        // when
        LinkInfoDto updatedLink =
                linkServiceImpl.updateLink(link.getId(), updateLinkDto, new Long[]{savedGroup.getId()}, null);

        // then
        assertAll("updatedLink",
                () -> assertEquals(updatedLink.getLinkName(),updateLinkDto.getLinkName()),
                () -> assertEquals(updatedLink.getLinkDesc(),updateLinkDto.getLinkDesc()),
                () -> assertEquals(updatedLink.getLinkUrl(),updateLinkDto.getLinkUrl()),
                () -> assertEquals(updatedLink.getImgUrl(),"/image/linkDefaultImg.png")
        );
    }

    @Test
    @DisplayName("링크를 삭제한다.")
    void deleteLinkTest() {
        // given
        Link link = createLinkEntity("깃허브 홈페이지","github.com","깃허브 홈페이지 설명");
        Link savedLink = linkRepository.save(link);

        // when
        linkServiceImpl.deleteLink(savedLink.getId());

        // then
        assertThat(linkRepository.findAll()).hasSize(0);
    }
}