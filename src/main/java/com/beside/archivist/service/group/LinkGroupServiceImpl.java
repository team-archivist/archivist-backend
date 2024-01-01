package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.LinkGroupDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.group.LinkGroup;
import com.beside.archivist.entity.link.Link;

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

    private final LinkRepository linkRepository;

    private final GroupRepository groupRepository;


    @Override
    public LinkGroupDto saveLinkGroup(LinkGroupDto linkGroupDto)  {
        Optional<Link> link = linkRepository.findById(linkGroupDto.getLinkId());
        Optional<Group> group = groupRepository.findById(linkGroupDto.getGroupId());

        LinkGroup linkGroup = LinkGroup.builder()
                .link(link.orElseThrow())
                .group(group.orElseThrow())
                .build();
        linkGroupRepository.save(linkGroup);
        return LinkGroupDto.builder()
                .linkGroupId(linkGroup.getId())
                .linkId(linkGroup.getLink().getId())
                .groupId(linkGroup.getGroup().getId())
                .build();
    }

    @Override
    public void deleteLinkGroup(Long linkGroupId) {
        linkGroupRepository.deleteById(linkGroupId);
    }
}