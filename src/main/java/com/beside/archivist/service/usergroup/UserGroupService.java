package com.beside.archivist.service.usergroup;

import com.beside.archivist.dto.group.GroupInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserGroupService {
    void saveUserGroup(Long groupId,String email, boolean isOwner);
    List<GroupInfoDto> getGroupDtoByUserId(Long userId, boolean isOwner);
    void deleteBookmark(Long userId, Long groupId);
    void checkDuplicateGroup(Long userId, Long groupId, boolean isOwner);
    void saveDefaultGroup(String email);
}
