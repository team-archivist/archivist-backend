package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.link.LinkDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface GroupService {
    GroupDto saveGroup(GroupDto groupDto, MultipartFile groupImgFile);

    GroupDto updateGroup(Long groupId, GroupDto groupDto, MultipartFile groupImgFile);

    void deleteGroup(Long groupId);

    GroupDto findGroupById(Long groupId);

    List<GroupDto> getGroupsByUserId(Long userId);

    List<LinkDto> getLinksByGroupId(Long groupId);
}
