package com.beside.archivist.controller.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.dto.link.LinkDto;
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
class LinkControllerTest {
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
                        .nickname("gudwls")
                        .password("1234")
                        .categories(List.of(Category.EXERCISE))
                        .build()
        );
    }

    LinkDto createLinkRequest(String linkUrl, String linkName, String linkDesc) {
        return LinkDto.builder()
                .linkUrl(linkUrl)
                .linkName(linkName)
                .linkDesc(linkDesc)
                .build();
    }

    @Test
    @DisplayName("회원이 링크를 생성한다.")
    void registerLinkTest() throws Exception {
        // given
        User savedUser = createUser("gudwls@test.com");
        String token = jwtTokenUtil.generateToken(savedUser.getEmail());
        LinkDto linkDto = createLinkRequest("www.naver.com","링크명", "링크설명");
        Long[] groupIds = {1L, 2L};

        // 요청 값이 @RequestPart 로 설정되어있기 때문에 MockMultipartFile 로 요청
        MockMultipartFile linkDtoFile = new MockMultipartFile(
                "linkDto",
                "123",
                "application/json",
                objectMapper.writeValueAsBytes(linkDto)
        );

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/link")
                        .file(linkDtoFile)
                        .param("groupId", "1", "2") // groupId 파라미터 추가
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.linkId").exists())
                .andExpect(jsonPath("$.linkUrl").value(linkDto.getLinkUrl()))
                .andExpect(jsonPath("$.linkName").value(linkDto.getLinkName()))
                .andExpect(jsonPath("$.linkDesc").value(linkDto.getLinkDesc()))
                .andExpect(jsonPath("$.imgUrl").value("/image/linkDefaultImg.png")) // 디폴트 값 링크 디폴트 이미지
        ;
    }


}