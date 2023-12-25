package com.beside.archivist.service.link;

import com.beside.archivist.entity.link.LinkImg;
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
    public LinkImg initializeDefaultImg() {
        return linkImgRepository.save(LinkImg.builder()
                .oriImgName("linkDefaultImg.png")
                .imgName("linkDefaultImg")
                .imgUrl("/image/linkDefaultImg.png")
                .build());
    }

    public LinkImg insertLinkImg(MultipartFile linkImgFile) {
        if(linkImgFile != null){

            String oriImgName = linkImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(linkImgLocation, linkImgFile);
            String imgUrl = "/images/links/"+imgName;
            return linkImgRepository.save(LinkImg.builder()
                    .oriImgName(oriImgName)
                    .imgName(imgName)
                    .imgUrl(imgUrl)
                    .build());
        }else {
            return null;
        }
    }

    @Override
    public void updateLinkImg(Long linkImgId, MultipartFile linkImgFile) {
        if(linkImgFile != null){
            LinkImg savedLinkImg = linkImgRepository.findById(linkImgId)
                    .orElseThrow(RuntimeException::new); // TO DO : 예외 처리
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
