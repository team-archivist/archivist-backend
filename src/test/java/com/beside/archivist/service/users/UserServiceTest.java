package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.entity.users.UserImg;
import com.beside.archivist.mapper.UserMapper;
import com.beside.archivist.repository.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapperImpl;
    @Mock
    UserImgService userImgServiceImpl;
    @InjectMocks
    UserServiceImpl userServiceImpl;

    UserDto userDto;
    User user;
    UserInfoDto userInfoDto;
    UserImg userImg;
    private final String EMAIL = "limnj@test.com";
    private final Long USER_ID = 1L;

    @BeforeEach // given
    void setUp() {
        List<Category> categories = List.of(Category.CULTURE, Category.EXERCISE);
        userDto = UserDto.builder() // 회원 정보 저장 시 요청 값
                .email(EMAIL)
                .nickname("limnj")
                .categories(categories)
                .build();

        userImg = UserImg.builder()
                .imgUrl("imgUrl")
                .imgName("imgName")
                .oriImgName("oriImgName")
                .build();

        user = User.builder() // 회원 Entity
                .email(EMAIL)
                .password(UUID.randomUUID().toString())
                .categories(categories)
                .nickname("limnj")
                .userImg(userImg)
                .build();

        userInfoDto = UserInfoDto.builder() // 회원 정보 저장 후 응답 값
                .userId(USER_ID)
                .email(EMAIL)
                .categories(categories)
                .nickname("limnj")
                .build();

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(user));
        given(userMapperImpl.toDto(any(User.class))).willReturn(userInfoDto);
    }

    @Test
    @DisplayName("회원 정보를 입력하여 회원가입한다.")
    public void givenUserDtoWhenSaveUserThenUserInfoDto(){
        // given
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.empty());

        // when
        UserInfoDto savedUser = userServiceImpl.saveUser(userDto,user.getUserImg());

        // then
        assertAll("userInfo",
                () ->  assertEquals(userInfoDto.getEmail(),savedUser.getEmail()),
                () -> assertEquals(userInfoDto.getNickname(),savedUser.getNickname()),
                ()-> assertEquals(userInfoDto.getCategories(),savedUser.getCategories())
        );

        verify(userRepository).save(any(User.class));
        verify(userMapperImpl).toDto(any(User.class));
    }

    @Test
    @DisplayName("이메일로 회원의 정보를 조회한다.")
    public void givenEmailWhenGetUserInfoThenUserInfoDto(){
        // given
        given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(user));

        // when
        UserInfoDto userInfo = userServiceImpl.getUserInfo(user.getEmail());

        // then
        assertEquals(EMAIL, userInfo.getEmail());

        verify(userRepository).findByEmail(any(String.class));
        verify(userMapperImpl).toDto(any(User.class));
    }

    @Test
    @DisplayName("회원 정보를 수정한다.")
    public void givenUserDtoWhenUpdateUserThenUserInfoDto(){
        // when
        UserInfoDto updateUser = userServiceImpl.updateUser(USER_ID, userDto, null);

        // then
        assertAll("userInfo",
                () ->  assertEquals(userInfoDto.getEmail(),updateUser.getEmail()),
                () -> assertEquals(userInfoDto.getNickname(),updateUser.getNickname()),
                ()-> assertEquals(userInfoDto.getCategories(),updateUser.getCategories())
        );

        verify(userRepository).findById(USER_ID);
        verify(userMapperImpl).toDto(any(User.class));
    }

    @Test
    @DisplayName("서비스를 (회원) 탈퇴한다.")
    public void givenUserIdWhenDeleteUser(){
        // when
        userServiceImpl.deleteUser(USER_ID);

        // then
        verify(userRepository).findById(USER_ID);
    }

    @Test
    @DisplayName("저장되어있는 모든 회원의 닉네임들을 조회한다.")
    public void whenGetNicknamesThenNicknameList(){
        // given
        List<String> expectedNicknames = List.of("limnj1","limnj2","limnj3");
        given(userRepository.getNicknames()).willReturn(expectedNicknames);

        // when
        List<String> actualNicknames = userServiceImpl.getNicknames();

        //then
        assertEquals(expectedNicknames.size(), actualNicknames.size());
        assertTrue(actualNicknames.containsAll(expectedNicknames));

        verify(userRepository).getNicknames();
    }

}