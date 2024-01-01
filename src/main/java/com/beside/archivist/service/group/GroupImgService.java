package com.beside.archivist.service.group;

import com.beside.archivist.entity.group.GroupImg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface GroupImgService {

    GroupImg initializeDefaultImg();
    GroupImg insertGroupImg(MultipartFile groupImgFile);

    void updateGroupImg(Long groupImgId, MultipartFile groupImgFile);
}