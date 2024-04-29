package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.redis.Users;
import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.entity.users.UserImg;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.users.InvalidCategoryNameException;
import com.beside.archivist.exception.users.UserAlreadyExistsException;
import com.beside.archivist.exception.users.UserNotFoundException;
import com.beside.archivist.mapper.UserMapper;
import com.beside.archivist.repository.redis.users.UserRedisRepository;
import com.beside.archivist.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapperImpl;
    private final UserImgService userImgServiceImpl;
    private final UserRedisRepository userRedisRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND));
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
    public UserInfoDto saveUser(UserDto userDto) {
        checkDuplicateUser(userDto.getEmail()); // 중복 회원 체크
        checkInvalidCategory(userDto.getCategories()); // 카테고리 null 값 체크

        User savedUser = userRepository.save(
                User.builder()
                        .email(userDto.getEmail())
                        .password(UUID.randomUUID().toString())
                        .categories(userDto.getCategories())
                        .nickname(userDto.getNickname())
                        .build()
        );

        /* 초기 디폴트 이미지 저장 */
        UserImg userImg = userImgServiceImpl.initializeDefaultImg();
        userImg.saveUser(savedUser);
        userImgServiceImpl.saveUserImg(userImg);

        return userMapperImpl.toDto(savedUser);
    }

    @Override
    public UserInfoDto getUserInfo(String email) {
        User findUser = getUserFromRedis(email)
                .orElseGet(() -> {
                    User user = getUserByEmail(email);
                    saveUserToRedis(user);
                    return user;
                });

        return userMapperImpl.toDto(findUser);
    }

    public Optional<User> getUserFromRedis(String email) {
        // Redis에서 해당 이메일을 키로 하는 사용자 정보 조회
        return userRedisRepository.findById(email)
                .map(users -> new User(users.getEmail(), users.getPassword(), users.getNickname(), users.getCategories()));
    }

    public void saveUserToRedis(User user) {
        // Redis에 사용자 정보 저장
        Users users = new Users(user.getEmail(), user.getNickname(), user.getPassword(), user.getCategories(), user.getUserImg());
        userRedisRepository.save(users);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public UserInfoDto updateUser(Long userId, UserDto userDto, MultipartFile userImgFile) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND));

        checkInvalidCategory(userDto.getCategories()); // 카테고리 null 값 체크
        user.updateUserInfo(userDto.getNickname(),userDto.getCategories()); // 유저 정보 update
        if (userImgFile != null){
            userImgServiceImpl.changeUserImg(user.getUserImg().getId(), userImgFile); // 유저 이미지 update
        }

        return userMapperImpl.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND));
        // 이메일을 마스킹하고 삭제
        user.updateUserEmail(maskEmail(user.getEmail()));
        user.setIsDeleted("Y");
        user.setDeletedAt(new Date().toInstant());
    }

    @Override
    public void checkDuplicateUser(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new UserAlreadyExistsException(ExceptionCode.USER_ALREADY_EXISTS);
        });
    }

    @Override
    public void checkInvalidCategory(List<Category> categories) {
        boolean containsNull = categories.stream().anyMatch(Objects::isNull);
        if (containsNull) {
            throw new InvalidCategoryNameException(ExceptionCode.INVALID_CATEGORY_NAME);
        }
    }
    
    @Override
    public List<String> getNicknames() {
        return userRepository.getNicknames();
    }

    @Override
    public String maskEmail(String email) {
        if (email == null || email.length() < 3) {
            // 이메일이 null이거나 길이가 3 미만인 경우 처리하지 않음
            return "";
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            // '@' 기호가 없거나 처음에 나오면 처리하지 않음
            return "";
        }

        String maskedPart = new String(new char[atIndex - 3]).replace('\0', '*');
        return email.charAt(0) + maskedPart + email.substring(atIndex - 2);
    }
}
