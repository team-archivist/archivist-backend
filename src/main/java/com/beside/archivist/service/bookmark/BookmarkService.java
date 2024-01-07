package com.beside.archivist.service.bookmark;

import com.beside.archivist.entity.bookmark.Bookmark;
import org.springframework.stereotype.Service;

@Service
public interface BookmarkService {
    void saveBookmark(Long groupId);
}
