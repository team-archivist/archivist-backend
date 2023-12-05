package com.beside.archivist.service.bookmark;

import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.bookmark.Bookmark;

import com.beside.archivist.repository.bookmark.BookmarkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;


    @Override
    public Bookmark saveBookmark(BookmarkDto bookmarkDto)  {

        Bookmark bookmark = Bookmark.builder()
                .bookUrl(bookmarkDto.getBookUrl())
                .bookName(bookmarkDto.getBookName())
                .bookDesc(bookmarkDto.getBookDesc())
                .user(bookmarkDto.getUser())
                .build();
        bookmarkRepository.save(bookmark);
        return bookmark;
    }

    @Override
    public Bookmark updateBookmark(Long bookmarkId, BookmarkDto bookmarkDto) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(RuntimeException::new);
        bookmarkRepository.deleteById(bookmarkId);
        return bookmark; // response 값 논의 필요
    }

    @Override
    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

}
