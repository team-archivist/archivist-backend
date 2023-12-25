package com.beside.archivist.service.link;

import com.beside.archivist.config.AuditConfig;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.link.Link;

import com.beside.archivist.entity.link.LinkImg;
import com.beside.archivist.entity.users.User;

import com.beside.archivist.repository.link.LinkRepository;
import com.beside.archivist.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private final LinkImgService linkImgService;

    private final AuditConfig auditConfig;

    private final UserRepository userRepository;


    @Override
    public LinkDto saveLink(LinkDto linkDto, MultipartFile linkImgFile)  {
        Optional<String> authentication = auditConfig.auditorProvider().getCurrentAuditor();
        String email = authentication.get();
        User user = userRepository.findByEmail(email).orElseThrow();

        LinkImg linkImg = null;
        if(linkImgFile == null){
            linkImg = linkImgService.initializeDefaultImg();
        }else{
            linkImg = linkImgService.insertLinkImg(linkImgFile);
        }

        Link link = Link.builder()
                .bookUrl(linkDto.getBookUrl())
                .bookName(linkDto.getBookName())
                .bookDesc(linkDto.getBookDesc())
                .user(user)
                .linkImg(linkImg)
                .build();
        linkRepository.save(link);
        return linkDto;
    }

    @Override
    public LinkDto updateLink(Long linkId, LinkDto linkDto, MultipartFile linkImgFile) {
        Link link = linkRepository.findById(linkId).orElseThrow(RuntimeException::new);

        if(linkImgFile != null){
            if(link.getLinkImg() == null){
                linkImgService.insertLinkImg(linkImgFile);
            }else{
                linkImgService.updateLinkImg(link.getLinkImg().getId(), linkImgFile);
            }
        }

        link.update(LinkDto.builder()
                .bookUrl(linkDto.getBookUrl())
                .bookName(linkDto.getBookName())
                .bookDesc(linkDto.getBookDesc())
                .build());
        return linkDto;
    }

    @Override
    public void deleteLink(Long linkId) {
        linkRepository.deleteById(linkId);
    }

    public LinkDto findLinkById(Long id){
        // 특정 북마크 ID에 해당하는 북마크 조회
        Link link = linkRepository.findById(id).orElseThrow();

        return LinkDto.builder()
                .bookUrl(link.getBookUrl())
                .bookName(link.getBookName())
                .bookDesc(link.getBookDesc())
                .imgUrl(link.getLinkImg().getImgUrl())
                .build();
    }

    public List<LinkDto> getLinksByUserId(Long userId){
        // 특정 사용자 ID에 해당하는 북마크 목록 조회
        List<Link> linkList = linkRepository.findByUsers_Id(userId);

        return linkList.stream()
                .map(m-> new LinkDto(m.getId(),
                        m.getBookUrl(),
                        m.getBookName(),
                        m.getBookDesc(),
                        m.getLinkImg().getImgUrl(),
                        m.getUsers().getId()))
                .collect(Collectors.toList());
    }
}