package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.repository.users.UserImgRepository;
import com.beside.archivist.repository.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserImgRepository userImgRepository;
    @Autowired
    UserService userServiceImpl;

    @AfterEach
    void tearDown() {
        userImgRepository.deleteAll();
        userRepository.deleteAll();
    }

    public UserDto createUserRequest(String email, List<Category> categories, String nickname){
        return UserDto.builder()
                .email(email)
                .categories(categories)
                .nickname(nickname)
                .build();
    }

    @Test
    @DisplayName("회원 정보를 입력하여 회원가입한다.")
    public void saveUserTest(){
        // given
        UserDto userRequest = createUserRequest("limnj@test.com",
                List.of(Category.CULTURE, Category.KNOWLEDGE), "limnj");

        // when
        UserInfoDto savedUser = userServiceImpl.saveUser(userRequest);

        // then
        assertAll("userRequest",
                () ->  assertEquals(userRequest.getEmail(),savedUser.getEmail()),
                () -> assertEquals(userRequest.getNickname(),savedUser.getNickname()),
                ()-> assertEquals(userRequest.getCategories(),savedUser.getCategories())
        );
    }

    @Test
    @DisplayName("이메일로 회원의 정보를 조회한다.")
    public void getUserInfoTest(){
        // given
        UserDto userRequest = createUserRequest("limnj@test.com",
                List.of(Category.CULTURE, Category.KNOWLEDGE), "limnj");
        UserInfoDto savedUser = userServiceImpl.saveUser(userRequest);

        // when
        UserInfoDto findUser = userServiceImpl.getUserInfo(userRequest.getEmail());

        // then
        assertEquals(userRequest.getEmail(), findUser.getEmail());
        assertAll("savedUser",
                () -> assertEquals(savedUser.getUserId(),findUser.getUserId()),
                () -> assertEquals(savedUser.getEmail(),findUser.getEmail()),
                () -> assertEquals(savedUser.getNickname(),findUser.getNickname()),
                () -> assertEquals(savedUser.getCategories(),findUser.getCategories()),
                () -> assertEquals(savedUser.getImgUrl(),findUser.getImgUrl()) // 초기 디폴트 이미지
        );
    }

    @Test
    @DisplayName("회원 닉네임과 카테고리를 수정한다.")
    public void updateUserTest(){
        // given
        UserDto userRequest = createUserRequest("limnj@test.com",
                List.of(Category.CULTURE, Category.KNOWLEDGE), "limnj");
        UserInfoDto savedUser = userServiceImpl.saveUser(userRequest);
        UserDto userDto = createUserRequest(savedUser.getEmail(),
                List.of(Category.LIFESTYLE,Category.ENTERTAINMENT), "updatedUser");

        // when
        UserInfoDto updateUser = userServiceImpl.updateUser(savedUser.getUserId(), userDto, null);

        // then
        assertAll("userDto",
                () ->  assertEquals(userDto.getEmail(),updateUser.getEmail()),
                () -> assertEquals(userDto.getNickname(),updateUser.getNickname()),
                ()-> assertEquals(userDto.getCategories(),updateUser.getCategories())
        );
    }

    @Test
    @DisplayName("서비스를 (회원) 탈퇴한다.")
    public void givenUserIdWhenDeleteUser(){
        // given
        UserDto userRequest = createUserRequest("limnj@test.com",
                List.of(Category.CULTURE, Category.KNOWLEDGE), "limnj");
        UserInfoDto savedUser = userServiceImpl.saveUser(userRequest);

        // when
        userServiceImpl.deleteUser(savedUser.getUserId());

        // then
        assertThat(userRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("저장되어있는 모든 회원의 닉네임들을 조회한다.")
    public void whenGetNicknamesThenNicknameList(){
        // given
        for (int i = 0; i < 3; i++) {
            UserDto userRequest = createUserRequest("limnj"+i+"@test.com",
                    List.of(Category.CULTURE, Category.KNOWLEDGE), "limnj"+i);
            userServiceImpl.saveUser(userRequest);
        }

        // when
        List<String> actualNicknames = userServiceImpl.getNicknames();

        //then
        assertEquals(actualNicknames.size(),3);
        assertTrue(actualNicknames.containsAll(List.of("limnj0","limnj1","limnj2")));
    }

}