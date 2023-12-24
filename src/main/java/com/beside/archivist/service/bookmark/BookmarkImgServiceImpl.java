package com.beside.archivist.service.bookmark;

import com.beside.archivist.entity.bookmark.BookmarkImg;
import com.beside.archivist.repository.bookmark.BookmarkImgRepository;
import com.beside.archivist.service.util.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service @Transactional
@RequiredArgsConstructor
public class BookmarkImgServiceImpl implements BookmarkImgService {

    @Value("${bookmarkImgLocation}")
    private String bookmarkImgLocation;
    private final BookmarkImgRepository bookmarkImgRepository;
    private final FileService fileService;

    @Override
    public BookmarkImg initializeDefaultImg() {
        return bookmarkImgRepository.save(BookmarkImg.builder()
                .oriImgName("bookmarkDefaultImg.png")
                .imgName("bookmarkDefaultImg")
                .imgUrl("/image/bookmarkDefaultImg.png")
                .build());
    }

    public BookmarkImg insertBookmarkImg(MultipartFile bookmarkImgFile) {
        if(bookmarkImgFile != null){

            String oriImgName = bookmarkImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(bookmarkImgLocation, bookmarkImgFile);
            String imgUrl = "/images/bookmarks/"+imgName;
            return bookmarkImgRepository.save(BookmarkImg.builder()
                    .oriImgName(oriImgName)
                    .imgName(imgName)
                    .imgUrl(imgUrl)
                    .build());
        }else {
            return null;
        }
    }

    @Override
    public void updateBookmarkImg(Long bookmarkImgId, MultipartFile bookmarkImgFile) {
        if(bookmarkImgFile != null){
            BookmarkImg savedBookmarkImg = bookmarkImgRepository.findById(bookmarkImgId)
                    .orElseThrow(RuntimeException::new); // TO DO : 예외 처리
            if(!(StringUtils.isEmpty(savedBookmarkImg.getImgName()) || StringUtils.isBlank(savedBookmarkImg.getImgName()))){
                fileService.deleteFile(bookmarkImgLocation, savedBookmarkImg.getImgName());
            }

            String oriImgName = bookmarkImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(bookmarkImgLocation, bookmarkImgFile);
            String imgUrl = "/images/bookmarks/"+imgName;
            savedBookmarkImg.updateBookmarkImg(imgName,oriImgName,imgUrl);
        }
    }
}
