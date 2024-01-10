package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.usergroup.UserGroup;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface GroupService {

    /** GROUP CRUD **/
    GroupDto saveGroup(GroupDto groupDto, MultipartFile groupImgFile);
    GroupDto findGroupById(Long groupId);
    GroupDto updateGroup(Long groupId, GroupDto groupDto, MultipartFile groupImgFile);
    Group getGroup(Long groupId);
    void deleteGroup(Long groupId);
    List<GroupDto> getGroupsByUserGroup(List<UserGroup> userGroups);
    List<LinkDto> getLinksByGroupId(Long groupId);
}
