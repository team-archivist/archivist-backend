package com.beside.archivist.controller.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.entity.users.Category;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    public UserDto createUserRequest(String email, List<Category> categories, String nickname){
        return UserDto.builder()
                .email(email)
                .categories(categories)
                .nickname(nickname)
                .build();
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @DisplayName("회원 가입을 한다.")
    @Test
    void registerUserTest() throws Exception {
        // given
        UserDto userRequest = createUserRequest("limnj@test.com",List.of(Category.BEAUTY),"limnjlimnjlimnjlimnj");
        String token = "Bearer "+jwtTokenUtil.generateToken(userRequest.getEmail());

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.email").value(userRequest.getEmail()))
                .andExpect(jsonPath("$.nickname").value(userRequest.getNickname()))
                .andExpect(jsonPath("$.categories[0]").value(userRequest.getCategories().get(0).getValue()))
        ;
    }

    @DisplayName("회원 가입 시 이메일, 닉네임, 카테고리는 필수 값이다.")
    @Test
    void registerUserWithoutRequiredValueTest() throws Exception {
        // given
        UserDto userRequest = createUserRequest(null, null,null);
        String token = "Bearer "+jwtTokenUtil.generateToken("limnj@test.com");

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message.email").value("이메일은 필수 값입니다."))
                .andExpect(jsonPath("$.message.nickname").value("닉네임을 입력해주세요."))
                .andExpect(jsonPath("$.message.categories").value("카테고리는 필수 값입니다."))
        ;
    }

    @DisplayName("회원 가입 시 이메일은 올바른 형식이어야 한다.")
    @Test
    void registerUserWithIncorrectEmailPatternTest() throws Exception{
        // given
        UserDto userRequest = createUserRequest("limnj@testcom", List.of(Category.LIFESTYLE),"limnj");
        String token = "Bearer "+jwtTokenUtil.generateToken(userRequest.getEmail());

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message.email").value("올바른 이메일 형식이 아닙니다"))
        ;
    }

    @DisplayName("회원 가입 시 닉네임은 최소 2자 이상이다.")
    @Test
    void registerUserWithNicknameLessThan2Test() throws Exception{
        // given
        UserDto userRequest = createUserRequest("limnj@test.com", List.of(Category.LIFESTYLE),"N");
        String token = "Bearer "+jwtTokenUtil.generateToken(userRequest.getEmail());

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message.nickname").value("닉네임은 2자 이상 입력해주세요"))
        ;
    }

    @DisplayName("회원 가입 시 닉네임은 최대 20자 이하이다.")
    @Test
    void registerUserWithNicknameMoreThan20Test() throws Exception{
        // given
        UserDto userRequest = createUserRequest("limnj@test.com", List.of(Category.LIFESTYLE),"안녕하세요안녕하세요안녕하세요안녕하세요a");
        String token = "Bearer "+jwtTokenUtil.generateToken(userRequest.getEmail());

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message.nickname").value("닉네임은 20자 이하로 입력해주세요"))
        ;
    }

    @DisplayName("회원 가입 시 닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능이다.")
    @Test
    void registerUserWithIncorrectNicknamePatternTest() throws Exception{
        // given
        UserDto userRequest = createUserRequest("limnj@test.com", List.of(Category.LIFESTYLE),"t !@test");
        String token = "Bearer "+jwtTokenUtil.generateToken(userRequest.getEmail());

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message.nickname").value("닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능해요"))
        ;
    }
}