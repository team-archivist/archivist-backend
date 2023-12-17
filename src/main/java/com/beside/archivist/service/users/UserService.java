package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.dto.users.UserInfoDto;
import com.beside.archivist.entity.users.UserImg;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService extends UserDetailsService {
    void adminLogin(String email, String password);
    UserInfoDto saveUser(UserDto userDto, UserImg userImg);
    UserInfoDto getUserInfo(Long userId);
    UserInfoDto updateUser(Long userId, UserDto userDto, MultipartFile userImgFile);
    void deleteUser(Long userId);
}
