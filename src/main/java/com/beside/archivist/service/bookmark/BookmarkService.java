package com.beside.archivist.service.bookmark;

import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.bookmark.Bookmark;
import org.springframework.stereotype.Service;

@Service
public interface BookmarkService {
    Bookmark saveBookmark(BookmarkDto bookmarkDto);
    Bookmark updateBookmark(Long bookmarkId, BookmarkDto bookmarkDto);
    void deleteBookmark(Long bookmarkId);
}
