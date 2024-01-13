package com.beside.archivist.service.link;

import com.beside.archivist.config.AuditConfig;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.link.Link;

import com.beside.archivist.entity.link.LinkImg;
import com.beside.archivist.entity.users.User;

import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.images.InvalidFileExtensionException;
import com.beside.archivist.mapper.LinkMapper;
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

    private final LinkMapper linkMapperImpl;

    private final LinkImgService linkImgService;

    private final AuditConfig auditConfig;

    private final UserRepository userRepository;

    @Override
    public LinkDto saveLink(LinkDto linkDto, MultipartFile linkImgFile)  {
        Optional<String> authentication = auditConfig.auditorProvider().getCurrentAuditor();
        String email = authentication.get();
        User user = userRepository.findByEmail(email).orElseThrow();

        LinkImg linkImg = null;
        if(linkImgFile == null || extractExtCheck(linkImgFile)){
            linkImg = linkImgService.initializeDefaultImg();
        }else{
            linkImg = linkImgService.insertLinkImg(linkImgFile);
        }

        Link link = Link.builder()
                .linkUrl(linkDto.getLinkUrl())
                .linkName(linkDto.getLinkName())
                .linkDesc(linkDto.getLinkDesc())
                .user(user)
                .linkImg(linkImg)
                .build();
        linkRepository.save(link);

        return linkMapperImpl.toDto(link);
    }

    private boolean extractExtCheck(MultipartFile imgFile) { // 확장자 추출
        String originalFilename = imgFile.getOriginalFilename();
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);

        // jpg, jpeg, png 제외 확장자 예외 처리
        return !ext.equalsIgnoreCase("jpg") && !ext.equalsIgnoreCase("jpeg") && !ext.equalsIgnoreCase("png");
    }

    @Override
    public LinkDto updateLink(Long linkId, LinkDto linkDto, MultipartFile linkImgFile) {
        Link link = linkRepository.findById(linkId).orElseThrow(RuntimeException::new);
        if(linkImgFile != null){
            if(extractExtCheck(linkImgFile)){
                throw new InvalidFileExtensionException(ExceptionCode.INVALID_FILE_EXTENSION);
            }
            if(link.getLinkImg() == null){
                linkImgService.insertLinkImg(linkImgFile);
            }else{
                linkImgService.updateLinkImg(link.getLinkImg().getId(), linkImgFile);
            }
        }
        link.update(linkDto);

        return linkMapperImpl.toDto(link);
    }

    @Override
    public void deleteLink(Long linkId) {
        linkRepository.deleteById(linkId);
    }

    public LinkDto findLinkById(Long id){
        // 특정 북마크 ID에 해당하는 북마크 조회
        Link link = linkRepository.findById(id).orElseThrow();

        return linkMapperImpl.toDto(link);
    }

    public List<LinkDto> getLinksByUserId(Long userId){
        // 특정 사용자 ID에 해당하는 북마크 목록 조회
        List<Link> linkList = linkRepository.findByUsers_Id(userId);

        return linkList.stream()
                .map(linkMapperImpl::toDto)
                .collect(Collectors.toList());
    }
}
