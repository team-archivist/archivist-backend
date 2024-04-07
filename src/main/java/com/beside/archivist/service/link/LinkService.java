package com.beside.archivist.service.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.group.Group;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface LinkService {
    LinkDto saveLink(LinkDto linkDto,Long[] groupId,String email, MultipartFile linkImgFile);
    LinkDto updateLink(Long linkId, LinkDto linkDto, Long[] groupId, MultipartFile linkImgFile);
    void deleteLink(Long linkId);

    LinkDto findLinkById(Long linkId);

    List<LinkDto>  getLinksByUserId(Long userId);

    List<Group> getGroupsByLinkId(Long linkId);
}
