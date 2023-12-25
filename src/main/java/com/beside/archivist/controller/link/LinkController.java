package com.beside.archivist.controller.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.service.link.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class LinkController {

    private final LinkService linkServiceImpl;

    @GetMapping("/user/link/{userId}")
    public ResponseEntity<List<LinkDto>> getUserLinkList(@PathVariable("userId") Long userId) {
        List<LinkDto> links = linkServiceImpl.getLinksByUserId(userId);
        return ResponseEntity.ok().body(links);
    }

    @GetMapping("/link/{id}")
    public ResponseEntity<?> findLinkById(@PathVariable("id") Long id) {
        LinkDto link = linkServiceImpl.findLinkById(id);
        return ResponseEntity.ok().body(link);
    }

    @GetMapping("/link/valid")
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


    @PostMapping("/link")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerLink(@RequestPart LinkDto linkDto,
                                              @RequestPart(value = "linkImgFile", required = false) MultipartFile linkImgFile) {
        LinkDto savedLink = linkServiceImpl.saveLink(linkDto,linkImgFile);
        return ResponseEntity.ok().body(savedLink);
    }

    @PostMapping ("/link/{linkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateLink(@PathVariable("linkId") Long linkId,
                                            @RequestPart LinkDto linkDto,
                                            @RequestPart(value = "linkImgFile", required = false) MultipartFile linkImgFile) {
        LinkDto updatedLink = linkServiceImpl.updateLink(linkId, linkDto, linkImgFile);
        return ResponseEntity.ok().body(updatedLink);
    }


    @DeleteMapping("/link/{linkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteLink(@PathVariable("linkId") Long linkId){
        linkServiceImpl.deleteLink(linkId);
        return ResponseEntity.ok().body("링크 삭제 완료.");
    }
}

