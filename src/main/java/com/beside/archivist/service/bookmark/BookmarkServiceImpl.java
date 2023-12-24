package com.beside.archivist.service.bookmark;

import com.beside.archivist.config.AuditConfig;
import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.entity.bookmark.Bookmark;

import com.beside.archivist.entity.bookmark.BookmarkImg;
import com.beside.archivist.entity.users.User;

import com.beside.archivist.repository.bookmark.BookmarkRepository;
import com.beside.archivist.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    private final BookmarkImgService bookmarkImgService;

    private final AuditConfig auditConfig;

    private final UserRepository userRepository;


    @Override
    public BookmarkDto saveBookmark(BookmarkDto bookmarkDto, MultipartFile bookmarkImgFile)  {
        Optional<String> authentication = auditConfig.auditorProvider().getCurrentAuditor();
        String email = authentication.get();
        User user = userRepository.findByEmail(email).orElseThrow();

        BookmarkImg bookmarkImg = null;
        if(bookmarkImgFile == null){
            bookmarkImg = bookmarkImgService.initializeDefaultImg();
        }else{
            bookmarkImg = bookmarkImgService.insertBookmarkImg(bookmarkImgFile);
        }

        Bookmark bookmark = Bookmark.builder()
                .bookUrl(bookmarkDto.getBookUrl())
                .bookName(bookmarkDto.getBookName())
                .bookDesc(bookmarkDto.getBookDesc())
                .user(user)
                .bookmarkImg(bookmarkImg)
                .build();
        bookmarkRepository.save(bookmark);
        return bookmarkDto;
    }

    @Override
    public BookmarkDto updateBookmark(Long bookmarkId, BookmarkDto bookmarkDto, MultipartFile bookmarkImgFile) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(RuntimeException::new);

        if(bookmarkImgFile != null){
            if(bookmark.getBookmarkImg() == null){
                bookmarkImgService.insertBookmarkImg(bookmarkImgFile);
            }else{
                bookmarkImgService.updateBookmarkImg(bookmark.getBookmarkImg().getId(), bookmarkImgFile);
            }
        }

        bookmark.update(BookmarkDto.builder()
                .bookUrl(bookmarkDto.getBookUrl())
                .bookName(bookmarkDto.getBookName())
                .bookDesc(bookmarkDto.getBookDesc())
                .build());
        return bookmarkDto;
    }

    @Override
    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

    public Optional<Bookmark> findBookmarkById(Long id){
        // 특정 북마크 ID에 해당하는 북마크 조회
        return bookmarkRepository.findById(id);
    }

    public List<Bookmark> getBookmarksByUserId(Long userId){
        // 특정 사용자 ID에 해당하는 북마크 목록 조회
        return bookmarkRepository.findByUsers_Id(userId);
    }
}
