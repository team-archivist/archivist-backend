package com.beside.archivist.dto.users;

import com.beside.archivist.entity.users.Category;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class UserDto {
    private String email;
    private String nickname;
    private List<Category> categories;
}
