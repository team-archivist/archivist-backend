package com.beside.archivist.service.bookmark;

import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.bookmark.Bookmark;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface BookmarkService {
    BookmarkDto saveBookmark(BookmarkDto bookmarkDto, MultipartFile bookmarkImgFile);
    BookmarkDto updateBookmark(Long bookmarkId, BookmarkDto bookmarkDto, MultipartFile bookmarkImgFile);
    void deleteBookmark(Long bookmarkId);

    Optional<Bookmark> findBookmarkById(Long bookmarkId);

    List<Bookmark>  getBookmarksByUserId(Long userId);
}
