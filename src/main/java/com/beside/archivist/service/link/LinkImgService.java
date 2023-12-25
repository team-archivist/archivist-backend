package com.beside.archivist.service.link;

import com.beside.archivist.entity.link.LinkImg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface LinkImgService {

    LinkImg initializeDefaultImg();
    LinkImg insertLinkImg(MultipartFile linkImgFile);

    void updateLinkImg(Long linkImgId, MultipartFile linkImgFile);
}