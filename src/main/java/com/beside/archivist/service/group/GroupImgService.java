package com.beside.archivist.service.group;

import com.beside.archivist.entity.group.GroupImg;
import com.beside.archivist.entity.link.LinkImg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface GroupImgService {

    GroupImg saveGroupImg(GroupImg groupImg);
    void changeToLinkImg(GroupImg groupImg, LinkImg linkImg);
    GroupImg insertGroupImg(GroupImg groupImg, MultipartFile groupImgFile);
    void changeLinkImg(Long groupImgId, MultipartFile groupImgFile);
}