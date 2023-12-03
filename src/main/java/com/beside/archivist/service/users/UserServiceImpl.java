package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
    public User saveUser(UserDto userDto) {
        /** TO DO
         * 기존에 있는 회원인지 검증하는 로직 추가
         */
        User user = User.builder()
                .email(userDto.getEmail())
                .password(UUID.randomUUID().toString())
                .categories(userDto.getCategories())
                .nickname(userDto.getNickname())
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new); // 추후 예외 커스텀
        user.update(userDto);
        return user; // response 값 논의 필요
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
