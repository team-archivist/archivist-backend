package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.LinkGroupDto;
import com.beside.archivist.entity.group.LinkGroup;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LinkGroupService {
    LinkGroup getLinkGroupById(Long userGroupId);
    LinkGroupDto saveLinkGroup(LinkGroupDto linkGroupDto);
    void deleteLinkGroup(Long groupId);
    void deleteLinkGroupByLinkId(Long linkId);
    List<LinkGroup> getLinkGroupsByGroupId(Long groupId);
    void changeGroupImg(Long groupId);
    boolean checkGroupLinkImgEquality(LinkGroup linkGroup);
}