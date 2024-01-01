package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.LinkGroupDto;
import org.springframework.stereotype.Service;

@Service
public interface LinkGroupService {
    LinkGroupDto saveLinkGroup(LinkGroupDto linkGroupDto);
    void deleteLinkGroup(Long groupId);
}