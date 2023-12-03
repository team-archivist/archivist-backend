package com.beside.archivist.service.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.link.Link;

import com.beside.archivist.repository.link.LinkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;


    @Override
    public Link saveLink(LinkDto linkDto)  {

        Link link = Link.builder()
                .email(linkDto.getEmail())
                .categories(linkDto.getCategories())
                .nickname(linkDto.getNickname())
                .user(linkDto.getUser())
                .build();
        linkRepository.save(link);
        return link;
    }

    @Override
    public Link updateLink(Long linkId, LinkDto linkDto) {
        Link link = linkRepository.findById(linkId).orElseThrow(RuntimeException::new);
        linkRepository.deleteById(linkId);
        return link; // response 값 논의 필요
    }

    @Override
    public void deleteLink(Long linkId) {
        linkRepository.deleteById(linkId);
    }

}
