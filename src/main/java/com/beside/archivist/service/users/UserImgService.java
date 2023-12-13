package com.beside.archivist.service.users;

import com.beside.archivist.entity.users.UserImg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserImgService {
    UserImg initializeDefaultImg();
    void updateUserImg(Long userImgId, MultipartFile userImgFile);

}
