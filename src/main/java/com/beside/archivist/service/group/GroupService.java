package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.group.GroupInfoDto;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.dto.link.LinkInfoDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.link.LinkImg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface GroupService {

    /** GROUP CRUD **/
    GroupInfoDto saveGroup(GroupDto groupDto, MultipartFile groupImgFile);
    GroupInfoDto findGroupById(Long groupId);
    GroupInfoDto updateGroup(Long groupId, GroupDto groupDto, MultipartFile groupImgFile);
    Group getGroup(Long groupId);
    void deleteGroup(Long groupId);
    List<LinkInfoDto> getLinksByGroupId(Long groupId);

    void changeToLinkImg(Long groupId, LinkImg linkImg);
    List<GroupInfoDto> getAllGroups();
}
