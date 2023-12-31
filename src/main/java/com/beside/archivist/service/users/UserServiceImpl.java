package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.entity.users.UserImg;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.users.UserAlreadyExistsException;
import com.beside.archivist.mapper.UserMapper;
import com.beside.archivist.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapperImpl;
    private final UserImgService userImgServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                true, true, true, true,
                new ArrayList<>()
        );
    }

    @Override
    public void adminLogin(String email, String password) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            User user = User.builder()
                            .email(email)
                            .password(password)
                            .build();
            userRepository.save(user);
        }
    }

    @Override
    public UserInfoDto saveUser(UserDto userDto, UserImg userImg) {
        checkDuplicateUser(userDto.getEmail()); // 중복 회원 체크

        User savedUser = userRepository.save(
                User.builder()
                        .email(userDto.getEmail())
                        .password(UUID.randomUUID().toString())
                        .categories(userDto.getCategories())
                        .nickname(userDto.getNickname())
                        .userImg(userImg) // 초기 디폴트 이미지 저장
                        .build()
        );

        return userMapperImpl.toDto(savedUser);
    }

    @Override
    public UserInfoDto getUserInfo(String email) {
        User findUser = getUserByEmail(email);
        return userMapperImpl.toDto(findUser);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(RuntimeException::new); // todo: 예외 처리
    }

    @Override
    public UserInfoDto updateUser(Long userId, UserDto userDto,MultipartFile userImgFile) {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new); // todo: 예외 처리
        user.updateUserInfo(userDto.getNickname(),userDto.getCategories()); // 유저 정보 update
        userImgServiceImpl.updateUserImg(user.getUserImg().getId(), userImgFile); // 유저 이미지 update

        return userMapperImpl.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void checkDuplicateUser(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new UserAlreadyExistsException(ExceptionCode.USER_ALREADY_EXISTS);
        });
    }
}
