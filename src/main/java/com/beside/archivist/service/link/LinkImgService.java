package com.beside.archivist.service.link;

import com.beside.archivist.entity.link.LinkImg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface LinkImgService {

    LinkImg saveLinkImg(LinkImg linkImg);
    LinkImg insertLinkImg(LinkImg linkImg,MultipartFile linkImgFile);

    void changeLinkImg(Long linkImgId, MultipartFile linkImgFile);
}