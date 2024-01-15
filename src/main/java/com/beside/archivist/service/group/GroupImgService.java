package com.beside.archivist.service.group;

import com.beside.archivist.entity.group.GroupImg;
import com.beside.archivist.entity.link.LinkImg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface GroupImgService {

    GroupImg initializeDefaultLinkImg();
    void changeToLinkImg(GroupImg groupImg, LinkImg linkImg);
    GroupImg insertGroupImg(MultipartFile groupImgFile);
    void updateGroupImg(Long groupImgId, MultipartFile groupImgFile);
}