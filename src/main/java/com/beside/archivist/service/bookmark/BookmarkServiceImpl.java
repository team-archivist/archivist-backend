package com.beside.archivist.service.bookmark;

import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.bookmark.Bookmark;

import com.beside.archivist.entity.bookmark.BookmarkImg;
import com.beside.archivist.repository.bookmark.BookmarkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    private final BookmarkImgServiceImpl bookmarkImgServiceImpl;

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
    public BookmarkDto updateBookmark(Long bookmarkId, BookmarkDto bookmarkDto, MultipartFile bookmarkImgFile) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(RuntimeException::new);

        BookmarkImg bookmarkImg = null;
        if(bookmark.getBookmarkImg() == null){
            bookmarkImg = bookmarkImgServiceImpl.insertBookmarkImg(bookmarkImgFile);
        }else{
            bookmarkImgServiceImpl.updateBookmarkImg(bookmark.getBookmarkImg().getId(), bookmarkImgFile);
            bookmarkImg = bookmark.getBookmarkImg();
        }

        bookmark.update(BookmarkDto.builder()
                .bookmarkId(bookmarkId)
                .bookUrl(bookmarkDto.getBookUrl())
                .bookName(bookmarkDto.getBookName())
                .bookDesc(bookmarkDto.getBookDesc())
                .bookmarkImg(bookmarkImg)
                .build());
        return bookmarkDto;
    }

    @Override
    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

}