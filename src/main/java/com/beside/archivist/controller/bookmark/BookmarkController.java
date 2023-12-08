package com.beside.archivist.controller.bookmark;

import com.beside.archivist.entity.bookmark.Bookmark;
import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.service.bookmark.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {
    
    private final BookmarkService bookmarkServiceImpl;

    @PostMapping("/bookmark")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerBookmark(@RequestBody BookmarkDto bookmarkDto) {
        Bookmark savedBookmark = bookmarkServiceImpl.saveBookmark(bookmarkDto);
        return ResponseEntity.ok().body(savedBookmark);
    }

    @PostMapping ("/bookmark/{bookmarkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateBookmark(@PathVariable("bookmarkId") Long bookmarkId, @RequestBody BookmarkDto bookmarkDto) {
        Bookmark updatedBookmark = bookmarkServiceImpl.updateBookmark(bookmarkId, bookmarkDto);
        return ResponseEntity.ok().body(updatedBookmark);
    }

    @DeleteMapping("/bookmark/{bookmarkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteBookmark(@PathVariable("bookmarkId") Long bookmarkId){
        bookmarkServiceImpl.deleteBookmark(bookmarkId);
        return ResponseEntity.ok().body("링크 삭제 완료.");
    }
}
