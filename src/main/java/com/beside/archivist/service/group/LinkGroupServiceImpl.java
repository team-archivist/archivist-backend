package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.LinkGroupDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.group.LinkGroup;
import com.beside.archivist.entity.link.Link;

import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.group.GroupNotFoundException;
import com.beside.archivist.exception.link.LinkNotFoundException;
import com.beside.archivist.mapper.LinkGroupMapper;
import com.beside.archivist.repository.group.GroupRepository;
import com.beside.archivist.repository.group.LinkGroupRepository;
import com.beside.archivist.repository.link.LinkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class LinkGroupServiceImpl implements LinkGroupService {

    private final LinkGroupRepository linkGroupRepository;

    private final LinkGroupMapper linkGroupMapperImpl;

    private final LinkRepository linkRepository;

    private final GroupRepository groupRepository;


    @Override
    public LinkGroupDto saveLinkGroup(LinkGroupDto linkGroupDto)  {
        Optional<Link> link = linkRepository.findById(linkGroupDto.getLinkId());
        Optional<Group> group = groupRepository.findById(linkGroupDto.getGroupId());

        LinkGroup linkGroup = LinkGroup.builder()
                .link(link.orElseThrow(() -> new LinkNotFoundException(ExceptionCode.LINK_NOT_FOUND)))
                .group(group.orElseThrow(() -> new GroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND)))
                .build();
        linkGroupRepository.save(linkGroup);
        return linkGroupMapperImpl.toDto(linkGroup);
    }

    @Override
    public void deleteLinkGroup(Long linkGroupId) {
        linkGroupRepository.deleteById(linkGroupId);
    }
}