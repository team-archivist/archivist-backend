package com.beside.archivist.service.usergroup;

import com.beside.archivist.entity.usergroup.UserGroup;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserGroupService {
    void saveUserGroup(Long groupId);

    List<UserGroup> getUserGroupsByUserId(Long userId);
}
