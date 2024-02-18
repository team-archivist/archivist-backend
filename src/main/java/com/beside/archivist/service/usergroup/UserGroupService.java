package com.beside.archivist.service.usergroup;

import com.beside.archivist.dto.group.GroupDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserGroupService {
    void saveUserGroup(Long groupId, boolean isOwner);
    List<GroupDto> getGroupDtoByUserId(Long userId, boolean isOwner);
    void deleteBookmark(Long userId, Long groupId);
    void checkDuplicateGroup(Long userId, Long groupId, boolean isOwner);
}
