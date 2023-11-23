package com.beside.archivist.service.users;

import com.beside.archivist.entity.users.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    void adminLogin(String email, String password);
}
