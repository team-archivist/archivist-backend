package com.beside.archivist.controller.bookmark;

import com.beside.archivist.entity.bookmark.Bookmark;
import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.service.bookmark.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {
    
    private final BookmarkService bookmarkServiceImpl;

    @GetMapping("/api/bookmark/{userId}")
    public ResponseEntity<List<Bookmark>> getUserBookmarkList(@PathVariable("userId") Long userId) {
        List<Bookmark> bookmarks = bookmarkServiceImpl.getBookmarksByUserId(userId);
        return new ResponseEntity<>(bookmarks, HttpStatus.OK);
    }

    @GetMapping("/api/bookmark/{id}")
    public ResponseEntity<?> findBookmarkById(@PathVariable("id") Long id) {
        Optional<Bookmark> bookmark = bookmarkServiceImpl.findBookmarkById(id);
        return bookmark.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/bookmark")
    public ResponseEntity<?> registerBookmark(@RequestBody BookmarkDto bookmarkDto) {
        Bookmark savedBookmark = bookmarkServiceImpl.saveBookmark(bookmarkDto);
        return ResponseEntity.ok().body(savedBookmark);
    }

    @PatchMapping ("/bookmark/{bookmarkId}")
    public ResponseEntity<?> updateBookmark(@PathVariable("bookmarkId") Long bookmarkId, @RequestBody BookmarkDto bookmarkDto) {
        Bookmark updatedBookmark = bookmarkServiceImpl.updateBookmark(bookmarkId, bookmarkDto);
        return ResponseEntity.ok().body(updatedBookmark);
    }

    @DeleteMapping("/bookmark/{bookmarkId}")
    public ResponseEntity<?> deleteBookmark(@PathVariable("bookmarkId") Long bookmarkId){
        bookmarkServiceImpl.deleteBookmark(bookmarkId);
        return ResponseEntity.ok().body("링크 삭제 완료.");
    }
}
