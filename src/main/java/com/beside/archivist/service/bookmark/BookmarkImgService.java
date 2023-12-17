package com.beside.archivist.service.bookmark;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface BookmarkImgService {
    void updateBookmarkImg(Long bookmarkImgId, MultipartFile bookmarkImgFile);

}
