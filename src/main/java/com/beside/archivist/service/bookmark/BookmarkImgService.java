package com.beside.archivist.service.bookmark;

import com.beside.archivist.entity.bookmark.BookmarkImg;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface BookmarkImgService {
    BookmarkImg insertBookmarkImg(MultipartFile bookmarkImgFile);

    void updateBookmarkImg(Long bookmarkImgId, MultipartFile bookmarkImgFile);
}