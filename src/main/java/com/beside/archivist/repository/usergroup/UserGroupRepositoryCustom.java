package com.beside.archivist.repository.usergroup;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.entity.group.Group;

import java.util.List;

public interface UserGroupRepositoryCustom {
    List<GroupDto> getGroupsByUserId(Long userId, boolean isOwner);
}
