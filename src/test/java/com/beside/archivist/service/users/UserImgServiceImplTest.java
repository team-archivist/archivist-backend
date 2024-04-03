package com.beside.archivist.service.users;

import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.entity.users.UserImg;
import com.beside.archivist.exception.images.ImageNotFoundException;
import com.beside.archivist.repository.users.UserImgRepository;
import com.beside.archivist.repository.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserImgServiceImplTest {

    @Autowired
    UserImgService userImgServiceImpl;
    @Autowired
    UserImgRepository userImgRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void teardown() {
        userImgRepository.deleteAll();
    }

    User createUser() {
        return userRepository.save(User.builder()
                .email("limnj@test.com")
                .nickname("limnj")
                .categories(List.of(Category.CAREER, Category.ENTERTAINMENT))
                .build()
        );
    }

    @Test
    @DisplayName("초기 프로필 이미지는 디폴트 이미지로 저장된다.")
    void saveUserDefaultImageTest() {
        // given
        UserImg userImg = userImgServiceImpl.initializeDefaultImg();
        userImg.saveUser(createUser());

        // when
        UserImg savedUserImg = userImgServiceImpl.saveUserImg(userImg);

        // then
        assertAll("savedUserImg",
                () ->  assertEquals(savedUserImg.getOriImgName(),"userDefaultImg.png"),
                () -> assertEquals(savedUserImg.getImgName(),"userDefaultImg"),
                ()-> assertEquals(savedUserImg.getImgUrl(),"/image/userDefaultImg.png")
        );
    }

    @Test
    @DisplayName("회원 프로필 이미지를 변경한다.")
    void changeUserImgTest() {
        // given
        UserImg userImg = userImgServiceImpl.initializeDefaultImg();
        userImg.saveUser(createUser());
        UserImg savedUserImg = userImgRepository.save(userImg);

        MockMultipartFile beChangedImg = new MockMultipartFile(
                "봄이다",
                "spring.jpg",
                String.valueOf(MediaType.IMAGE_JPEG),
                "Spring!".getBytes()
        );

        // when
        userImgServiceImpl.changeUserImg(savedUserImg.getId(), beChangedImg);

        // then
        UserImg changedUserImg = userImgRepository.findById(savedUserImg.getId()).orElseThrow();
        assertAll("changedUserImg",
                () ->  assertEquals(changedUserImg.getOriImgName(),beChangedImg.getOriginalFilename())
        );
    }

    @Test
    @DisplayName("변경하려는 프로필 이미지가 없으면 예외가 발생한다.")
    void imageNotFoundExceptionTest() {
        // given
        Long userImgId = 1L;

        // when // then
        assertThatThrownBy(() ->
                userImgServiceImpl.changeUserImg(userImgId, new MockMultipartFile("userImg", "userImg".getBytes()))
        ).isInstanceOf(ImageNotFoundException.class);
    }
}