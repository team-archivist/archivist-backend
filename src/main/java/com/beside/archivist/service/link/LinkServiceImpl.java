package com.beside.archivist.service.link;

import com.beside.archivist.dto.group.LinkGroupDto;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.group.LinkGroup;
import com.beside.archivist.entity.link.Link;

import com.beside.archivist.entity.link.LinkImg;
import com.beside.archivist.entity.users.User;

import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.link.LinkNotFoundException;
import com.beside.archivist.exception.users.MissingAuthenticationException;
import com.beside.archivist.exception.users.UserNotFoundException;
import com.beside.archivist.mapper.LinkMapper;
import com.beside.archivist.repository.link.LinkRepository;
import com.beside.archivist.repository.users.UserRepository;
import com.beside.archivist.service.group.LinkGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private final LinkMapper linkMapperImpl;

    private final LinkImgService linkImgService;

    private final LinkGroupService linkGroupService;

    private final UserRepository userRepository;

    @Override
    public LinkDto saveLink(LinkDto linkDto, Long[] groupIds, String email, MultipartFile linkImgFile)  {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND));

        Link savedLink = linkRepository.save(
                Link.builder()
                        .linkUrl(linkDto.getLinkUrl())
                        .linkName(linkDto.getLinkName())
                        .linkDesc(linkDto.getLinkDesc())
                        .user(user)
                        .build());

        LinkImg linkImg = LinkImg.initializeDefaultLinkImg();
        linkImg.saveLink(savedLink);

        if(linkImgFile != null){
            linkImgService.insertLinkImg(linkImg, linkImgFile);
        }else{
            linkImgService.saveLinkImg(linkImg);
        }

        if(groupIds != null){
            linkGroupService.deleteLinkGroupByLinkId(savedLink.getId());
            for(Long groupId : groupIds){
                LinkGroupDto linkGroupDto = LinkGroupDto.builder().groupId(groupId).linkId(savedLink.getId()).build();
                linkGroupService.saveLinkGroup(linkGroupDto);
            }
        }

        return linkMapperImpl.toDto(savedLink);
    }

    @Override
    public LinkDto updateLink(Long linkId, LinkDto linkDto, Long[] groupIds, MultipartFile linkImgFile) {
        Link link = linkRepository.findById(linkId).orElseThrow(() -> new LinkNotFoundException(ExceptionCode.LINK_NOT_FOUND));

        if(linkImgFile != null){
            linkImgService.changeLinkImg(link.getLinkImg().getId(), linkImgFile);
        }
        link.update(linkDto);

        if(groupIds != null){
            linkGroupService.deleteLinkGroupByLinkId(link.getId());
            for(Long groupId : groupIds){
                LinkGroupDto linkGroupDto = LinkGroupDto.builder().groupId(groupId).linkId(link.getId()).build();
                linkGroupService.saveLinkGroup(linkGroupDto);
            }
        }

        return linkMapperImpl.toDto(link);
    }

    @Override
    public void deleteLink(Long linkId) {
        linkRepository.deleteById(linkId);
    }
    @Override
    public LinkDto findLinkById(Long linkId){
        // 특정 북마크 ID에 해당하는 북마크 조회
        Link link = linkRepository.findById(linkId).orElseThrow(() -> new LinkNotFoundException(ExceptionCode.LINK_NOT_FOUND));

        return linkMapperImpl.toDto(link);
    }
    @Override
    public List<LinkDto> getLinksByUserId(Long userId){
        // 특정 사용자 ID에 해당하는 북마크 목록 조회
        List<Link> linkList = linkRepository.findByUsers_Id(userId);

        return linkList.stream()
                .map(linkMapperImpl::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<Group> getGroupsByLinkId(Long linkId) {
        Optional<Link> link = linkRepository.findById(linkId);
        if(link.isPresent()) {
            List<LinkGroup> linkGroups = link.get().getLinkGroups();
            return linkGroups.stream().map(LinkGroup::getGroup).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
