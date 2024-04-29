package com.beside.archivist.service.link;

import com.beside.archivist.entity.group.GroupImg;
import com.beside.archivist.entity.link.LinkImg;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.images.ImageNotFoundException;
import com.beside.archivist.repository.link.LinkImgRepository;
import com.beside.archivist.service.util.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service @Transactional
@RequiredArgsConstructor
public class LinkImgServiceImpl implements LinkImgService {

    @Value("${linkImgLocation}")
    private String linkImgLocation;
    private final LinkImgRepository linkImgRepository;
    private final FileService fileService;

    @Override
    public LinkImg saveLinkImg(LinkImg linkImg) {
        LinkImg savedLinkImg = linkImgRepository.save(linkImg);
        savedLinkImg.getLink().saveLinkImg(savedLinkImg);
        return savedLinkImg;
    }

    @Override
    public LinkImg insertLinkImg(LinkImg linkImg,MultipartFile linkImgFile) {
        String oriImgName = linkImgFile.getOriginalFilename();
        String imgName = fileService.uploadFile(linkImgLocation, linkImgFile);
        String imgUrl = "/images/links/"+imgName;
        linkImg.updateLinkImg(imgName,oriImgName,imgUrl);

        return saveLinkImg(linkImg);
    }

    @Override
    public void changeLinkImg(Long linkImgId, MultipartFile linkImgFile) {
        if(linkImgFile != null){
            LinkImg savedLinkImg = linkImgRepository.findById(linkImgId)
                    .orElseThrow(() -> new ImageNotFoundException(ExceptionCode.IMAGE_NOT_FOUND));
            if(!(StringUtils.isEmpty(savedLinkImg.getImgName()) || StringUtils.isBlank(savedLinkImg.getImgName()))){
                fileService.deleteFile(linkImgLocation, savedLinkImg.getImgName());
            }

            String oriImgName = linkImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(linkImgLocation, linkImgFile);
            String imgUrl = "/images/links/"+imgName;
            savedLinkImg.updateLinkImg(imgName,oriImgName,imgUrl);
        }
    }
}
