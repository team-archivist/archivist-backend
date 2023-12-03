package com.beside.archivist.service.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.link.Link;
import org.springframework.stereotype.Service;

@Service
public interface LinkService {
    Link saveLink(LinkDto linkDto);
    Link updateLink(Long linkId, LinkDto linkDto);
    void deleteLink(Long linkId);
}
