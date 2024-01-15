package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.LinkGroupDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.group.LinkGroup;
import com.beside.archivist.entity.link.Link;

import com.beside.archivist.entity.link.LinkImg;
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
    public LinkGroup getLinkGroupById(Long userGroupId) {
        return linkGroupRepository.findById(userGroupId).orElseThrow(); // todo: 예외 처리
    }

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
        LinkGroup linkGroup = getLinkGroupById(linkGroupId);
        boolean existImageCheck = checkGroupLinkImgEquality(linkGroup); // 기존에 저장된 이미지가 삭제하는 링크 이미지인 경우 true

        linkGroupRepository.deleteById(linkGroupId);

        if(existImageCheck){ // 이미지 재설정
            changeGroupImg(linkGroup.getGroup().getId());
        }
    }

    @Override
    public List<LinkGroup> getLinkGroupsByGroupId(Long groupId) {
        return linkGroupRepository.findByGroup_Id(groupId);
    }

    /** link가 그룹에서 빠졌을 때나 link 가 삭제되었을 경우
     *  todo: 링크 이미지가 바뀌었을 때도 추가
     * **/
    @Override
    public void changeGroupImg(Long groupId){
        List<LinkGroup> linkGroups = getLinkGroupsByGroupId(groupId);
        if(linkGroups.isEmpty()){ // 1. 아무것도 없을 때는 디폴트 이미지로
            groupServiceImpl.changeToLinkImg(groupId, LinkImg.initializeDefaultLinkImg());
            return;
        } else if(!linkGroups.get(0).getGroup().getGroupImg().getImgUrl().equals("/image/linkDefaultImg.png")){
            return; // 2. 링크 이미지로 되어있을 때는 변경 X
        }

        Optional<Link> firstLink = linkGroups.stream()
                .map(LinkGroup::getLink)
                .filter(link -> !link.getLinkImg().getImgUrl().equals("/image/linkDefaultImg.png"))
                .findFirst(); // 디폴트 이미지가 아닌 것 중 첫번째 Link return

        firstLink.ifPresent(link -> groupServiceImpl.changeToLinkImg(groupId, link.getLinkImg()));
    }

    @Override // 그룹 내에서 삭제하는 링크 이미지와 그룹 이미지가 같을 때
    public boolean checkGroupLinkImgEquality(LinkGroup linkGroup) {
        String linkImg = linkGroup.getLink().getLinkImg().getImgUrl();
        String groupImg = linkGroup.getGroup().getGroupImg().getImgUrl();
        return linkImg.equals(groupImg);
    }
}