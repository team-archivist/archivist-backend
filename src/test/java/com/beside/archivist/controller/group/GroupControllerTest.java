package com.beside.archivist.controller.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.repository.users.UserRepository;
import com.beside.archivist.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GroupControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    User createUser(String email){
        return userRepository.save(
                User.builder()
                        .email(email)
                        .nickname("limnj")
                        .password("1234")
                        .categories(List.of(Category.EXERCISE))
                        .build()
        );
    }

    GroupDto createGroupRequest(String groupName, String groupDesc, List<Category> categories) {
        return GroupDto.builder()
                .groupName(groupName)
                .groupDesc(groupDesc)
                .categories(categories)
                .build();
    }

    @Test
    @DisplayName("회원이 그룹을 생성한다.")
    void registerGroupTest() throws Exception {
        // given
        User savedUser = createUser("limnj@test.com");
        String token = jwtTokenUtil.generateToken(savedUser.getEmail());
        GroupDto groupDto = createGroupRequest("그룹명", "그룹설명", List.of(Category.LIFESTYLE));

        // 요청 값이 @RequestPart 로 설정되어있기 때문에 MockMultipartFile 로 요청
        MockMultipartFile groupDtoFile = new MockMultipartFile(
                "groupDto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(groupDto)
        );

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/group")
                        .file(groupDtoFile)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.groupId").exists())
                .andExpect(jsonPath("$.groupName").value(groupDto.getGroupName()))
                .andExpect(jsonPath("$.groupDesc").value(groupDto.getGroupDesc()))
                .andExpect(jsonPath("$.isGroupPublic").value("Y")) // 디폴트 값 Y
                .andExpect(jsonPath("$.imgUrl").value("/image/linkDefaultImg.png")) // 디폴트 값 링크 디폴트 이미지
                .andExpect(jsonPath("$.linkCount").value(0)) // 디폴트 값 0
        ;
    }

    @Test
    @DisplayName("그룹 생성 시 그룹명은 필수 값이다.")
    void createGroupWithoutRequiredValueTest() throws Exception {
        // given
        User savedUser = createUser("limnj@test.com");
        String token = jwtTokenUtil.generateToken(savedUser.getEmail());
        GroupDto groupDto = createGroupRequest(null, null, null);
        MockMultipartFile groupDtoFile = new MockMultipartFile(
                "groupDto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(groupDto)
        );

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/group")
                        .file(groupDtoFile)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message.groupName").value("그룹 이름은 공백일 수 없습니다."))
        ;
    }

    @Test
    @DisplayName("그룹 생성 시 그룹명은 100자 이하, 그룹 설명은 400자 이하이다.")
    void createGroupWithGroupNameAndGroupDescriptionLessThanX00Test() throws Exception {
        // given
        User savedUser = createUser("limnj@test.com");
        String token = jwtTokenUtil.generateToken(savedUser.getEmail());
        GroupDto groupDto = createGroupRequest("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001",
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001", null);
        MockMultipartFile groupDtoFile = new MockMultipartFile(
                "groupDto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(groupDto)
        );

        // when // then
        assertThat(groupDto.getGroupName().length()).isEqualTo(101);
        assertThat(groupDto.getGroupDesc().length()).isEqualTo(401);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/group")
                        .file(groupDtoFile)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message.groupName").value("그룹 이름은 1자에서 100자 사이 여야 합니다."))
                .andExpect(jsonPath("$.message.groupDesc").value("그룹 설명은 400자 이내 이여야 합니다."))
        ;
    }
}