package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.entity.users.UserImg;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    /* 회원 CRUD */
    void adminLogin(String email, String password);
    UserInfoDto saveUser(UserDto userDto, UserImg userImg);
    UserInfoDto getUserInfo(String email);
    UserInfoDto updateUser(Long userId, UserDto userDto, MultipartFile userImgFile);
    User getUserByEmail(String email);
    void deleteUser(Long userId);

    /* 회원 유효성 검증 */
    void checkDuplicateUser(String email);
    List<String> getNicknames();
}
