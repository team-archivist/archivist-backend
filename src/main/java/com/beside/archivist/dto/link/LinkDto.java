package com.beside.archivist.dto.link;

import com.beside.archivist.entity.users.Category;
import com.beside.archivist.entity.users.User;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class LinkDto {
    private String email;
    private String nickname;
    private List<Category> categories;
    private User user;
}
