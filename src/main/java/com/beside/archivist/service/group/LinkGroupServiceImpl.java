package com.beside.archivist.service.group;

import com.beside.archivist.dto.group.LinkGroupDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.group.LinkGroup;
import com.beside.archivist.entity.link.Link;

import com.beside.archivist.entity.link.LinkImg;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.group.GroupNotFoundException;
import com.beside.archivist.exception.link.LinkInGroupNotFoundException;
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
    private final LinkRepository linkRepository;
    private final GroupRepository groupRepository;
    private final LinkGroupMapper linkGroupMapperImpl;
    private final GroupService groupServiceImpl;

    @Override
    public LinkGroup getLinkGroupById(Long linkGroupId) {
        return linkGroupRepository.findById(linkGroupId).orElseThrow(
                () -> new LinkInGroupNotFoundException(ExceptionCode.LINK_IN_GROUP_NOT_FOUND));
    }

    @Override
    public LinkGroupDto saveLinkGroup(LinkGroupDto linkGroupDto)  {
        Link link = linkRepository.findById(linkGroupDto.getLinkId()).orElseThrow(
                () -> new LinkInGroupNotFoundException(ExceptionCode.LINK_NOT_FOUND));
        Group group = groupRepository.findById(linkGroupDto.getGroupId()).orElseThrow(
                () -> new LinkInGroupNotFoundException(ExceptionCode.GROUP_NOT_FOUND));

        LinkGroup linkGroup = LinkGroup.builder()
                .link(link)
                .group(group)
                .build();

        // 양방향 저장
        LinkGroup savedLinkGroup = linkGroupRepository.save(linkGroup);
        savedLinkGroup.getGroup().addLinkGroup(savedLinkGroup);
        savedLinkGroup.getLink().addLinkGroup(savedLinkGroup);

        return linkGroupMapperImpl.toDto(savedLinkGroup);
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
    public void deleteLinkGroupByLinkId(Long linkId) {
        List<LinkGroup> linkGroupList = linkGroupRepository.findByLink_Id(linkId);

        for(LinkGroup linkGroup : linkGroupList){
            boolean existImageCheck = checkGroupLinkImgEquality(linkGroup); // 기존에 저장된 이미지가 삭제하는 링크 이미지인 경우 true

            linkGroupRepository.deleteById(linkGroup.getId());

            if(existImageCheck){ // 이미지 재설정
                changeGroupImg(linkGroup.getGroup().getId());
            }
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

    @Override
    public void changeGroupImgArray(Long[] groupIdArray) {  // 그룹 ID 가 여러 개인 경우
        for (Long groupId : groupIdArray) {
            changeGroupImg(groupId);
        }
    }

    @Override // 그룹 내에서 삭제하는 링크 이미지와 그룹 이미지가 같을 때
    public boolean checkGroupLinkImgEquality(LinkGroup linkGroup) {
        String linkImg = linkGroup.getLink().getLinkImg().getImgUrl();
        String groupImg = linkGroup.getGroup().getGroupImg().getImgUrl();
        return linkImg.equals(groupImg);
    }
}