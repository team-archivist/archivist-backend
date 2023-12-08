package com.beside.archivist.service.users;

import com.beside.archivist.dto.users.UserDto;
import com.beside.archivist.entity.users.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    void adminLogin(String email, String password);
    User saveUser(UserDto userDto);
    User getUserInfo(Long userId);
    User updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);
}
