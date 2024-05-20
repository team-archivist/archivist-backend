package com.beside.archivist.repository.usergroup;

import com.beside.archivist.dto.group.GroupInfoDto;


import java.util.List;

public interface UserGroupRepositoryCustom {
    List<GroupInfoDto> getGroupsByUserId(Long userId, boolean isOwner);
}
