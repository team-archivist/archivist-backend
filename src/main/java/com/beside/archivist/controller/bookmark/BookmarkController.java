package com.beside.archivist.controller.bookmark;

import com.beside.archivist.entity.bookmark.Bookmark;
import com.beside.archivist.dto.bookmark.BookmarkDto;
import com.beside.archivist.service.bookmark.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

    private final BookmarkService bookmarkServiceImpl;

    @GetMapping("/user/bookmark/{userId}")
    public ResponseEntity<List<Bookmark>> getUserBookmarkList(@PathVariable("userId") Long userId) {
        List<Bookmark> bookmarks = bookmarkServiceImpl.getBookmarksByUserId(userId);
        return ResponseEntity.ok().body(bookmarks);
    }

    @GetMapping("/bookmark/{id}")
    public ResponseEntity<?> findBookmarkById(@PathVariable("id") Long id) {
        Optional<Bookmark> bookmark = bookmarkServiceImpl.findBookmarkById(id);
        return ResponseEntity.ok().body(bookmark);
    }

    @GetMapping("/bookmark/valid")
    public ResponseEntity<?> urlValidation(@RequestParam String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        String validAt = "";
        if (urlValidator.isValid(url)) {
            validAt = "Valid URL";
        } else {
            validAt = "Invalid URL";
        }
        return ResponseEntity.ok().body(validAt);
    }


    @PostMapping("/bookmark")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerBookmark(@RequestBody BookmarkDto bookmarkDto) {
        BookmarkDto savedBookmark = bookmarkServiceImpl.saveBookmark(bookmarkDto);
        return ResponseEntity.ok().body(savedBookmark);
    }

    @PostMapping ("/bookmark/{bookmarkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateBookmark(@PathVariable("bookmarkId") Long bookmarkId,
                                            @RequestPart BookmarkDto bookmarkDto,
                                            @RequestPart(value = "bookmarkImgFile", required = false) MultipartFile bookmarkImgFile) {
        BookmarkDto updatedBookmark = bookmarkServiceImpl.updateBookmark(bookmarkId, bookmarkDto, bookmarkImgFile);
        return ResponseEntity.ok().body(updatedBookmark);
    }


    @DeleteMapping("/bookmark/{bookmarkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteBookmark(@PathVariable("bookmarkId") Long bookmarkId){
        bookmarkServiceImpl.deleteBookmark(bookmarkId);
        return ResponseEntity.ok().body("링크 삭제 완료.");
    }
}

