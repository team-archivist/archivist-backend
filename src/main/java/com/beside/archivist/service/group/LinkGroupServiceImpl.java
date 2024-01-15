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

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class LinkGroupServiceImpl implements LinkGroupService {

    private final LinkGroupRepository linkGroupRepository;

    private final LinkGroupMapper linkGroupMapperImpl;
    private final GroupService groupServiceImpl;

    private final LinkRepository linkRepository; // 서비스 구현체 가져와주세요! ( 비즈니스 로직과 데이터 접근 로직을 분리하기 위함 )

    private final GroupRepository groupRepository; // 서비스 구현체 가져와주세요! ( 비즈니스 로직과 데이터 접근 로직을 분리하기 위함 )


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

    @Override
    public List<LinkGroup> getLinkGroupsByGroupId(Long groupId) {
        return linkGroupRepository.findByGroup_Id(groupId);
    }
    @Override
    public void changeGroupImg(Long groupId){
        List<LinkGroup> linkGroups = getLinkGroupsByGroupId(groupId);
        if(!linkGroups.get(0).getGroup().getGroupImg().getImgUrl().equals("/image/linkDefaultImg.png")){
            return; // 링크 이미지로 되어있을 때는 return; ( 변경 X )
        }
        Optional<Link> firstLink = linkGroups.stream()
                .map(LinkGroup::getLink)
                .filter(link -> !link.getLinkUrl().equals("/image/linkDefaultImg.png"))
                .findFirst(); // 디폴트 이미지가 아닌 것 중 첫번째 Link return
        firstLink.ifPresent(link -> groupServiceImpl.changeToLinkImg(groupId, link.getLinkImg()));
    }

}