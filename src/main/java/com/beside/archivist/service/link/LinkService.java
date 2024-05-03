package com.beside.archivist.service.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.dto.link.LinkInfoDto;
import com.beside.archivist.entity.group.Group;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface LinkService {
    LinkInfoDto saveLink(LinkDto linkDto,Long[] groupIds,String email, MultipartFile linkImgFile);
    LinkInfoDto updateLink(Long linkId, LinkDto linkDto, Long[] groupIds, MultipartFile linkImgFile);
    void deleteLink(Long linkId);

    LinkInfoDto findLinkById(Long linkId);

    List<LinkInfoDto>  getLinksByUserId(Long userId);

    List<Group> getGroupsByLinkId(Long linkId);
}
