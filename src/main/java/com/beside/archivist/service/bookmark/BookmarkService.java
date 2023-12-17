package com.beside.archivist.service.bookmark;

import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.bookmark.Bookmark;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface BookmarkService {
    Bookmark saveBookmark(BookmarkDto bookmarkDto);
    BookmarkDto updateBookmark(Long bookmarkId, BookmarkDto bookmarkDto, MultipartFile bookmarkImgFile);
    void deleteBookmark(Long bookmarkId);
}
