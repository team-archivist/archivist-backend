package com.beside.archivist.entity;

import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.link.Link;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.repository.group.GroupRepository;
import com.beside.archivist.repository.link.LinkRepository;
import com.beside.archivist.repository.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BaseEntityTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private GroupRepository groupRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        linkRepository.deleteAllInBatch();
        groupRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("객체 생성시 isDeleted 필드 값은 'N'이다.")
    void isDeletedDefaultValueTest() {
        // given
        User user = User.builder()
                .email("limnj@test.com")
                .nickname("limnj")
                .password("1234")
                .categories(List.of(Category.LIFESTYLE))
                .build();
        Link link = Link.builder()
                .linkName("linkName")
                .linkDesc("linkDesc")
                .linkUrl("test.com")
                .build();
        Group group = Group.builder()
                .groupName("groupName")
                .groupDesc("groupDesc")
                .categories(List.of(Category.LIFESTYLE))
                .build();

        // when
        User savedUser = userRepository.save(user);
        Link savedLink = linkRepository.save(link);
        Group savedGroup = groupRepository.save(group);

        // then
        assertEquals("N",savedUser.getIsDeleted());
        assertEquals("N",savedLink.getIsDeleted());
        assertEquals("N",savedGroup.getIsDeleted());
    }

}