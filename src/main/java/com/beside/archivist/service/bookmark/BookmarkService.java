package com.beside.archivist.service.bookmark;

import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.bookmark.Bookmark;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookmarkService {
    Bookmark saveBookmark(BookmarkDto bookmarkDto);
    Bookmark updateBookmark(Long bookmarkId, BookmarkDto bookmarkDto);
    void deleteBookmark(Long bookmarkId);

    Optional<Bookmark> findBookmarkById(Long bookmarkId);

    List<Bookmark>  getBookmarksByUserId(Long userId);
}
