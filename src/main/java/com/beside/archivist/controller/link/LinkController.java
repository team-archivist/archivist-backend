package com.beside.archivist.controller.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.dto.link.LinkInfoDto;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.users.MissingAuthenticationException;
import com.beside.archivist.service.group.LinkGroupService;
import com.beside.archivist.service.link.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class LinkController {

    private final LinkService linkServiceImpl;
    private final LinkGroupService linkGroupServiceImpl;

    @GetMapping("/user/link/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "회원이 저장한 링크 모두 조회 API")
    public ResponseEntity<List<LinkInfoDto>> getUserLinkList(@PathVariable("userId") Long userId) {
        List<LinkInfoDto> links = linkServiceImpl.getLinksByUserId(userId);
        return ResponseEntity.ok().body(links);
    }

    @GetMapping("/link/{linkId}")
    @Operation(summary = "특정 링크 상세 조회 API")
    public ResponseEntity<?> findLinkById(@PathVariable("linkId") Long linkId) {
        LinkInfoDto linkInfoDto = linkServiceImpl.findLinkById(linkId);
        return ResponseEntity.ok().body(linkInfoDto);
    }

    @GetMapping("/link/valid")
    @Operation(summary = "URL 유효성 검증 API")
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
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "웹 링크 저장 API")
    public ResponseEntity<?> registerLink(@RequestPart @Valid LinkDto linkDto,
                                          Authentication authentication,
                                          @RequestPart(value = "groupId") Long[] groupIds,
                                          @RequestPart(value = "linkImgFile", required = false) MultipartFile linkImgFile) {
        if (authentication == null){
            throw new MissingAuthenticationException(ExceptionCode.MISSING_AUTHENTICATION);
        }

        LinkInfoDto savedLink = linkServiceImpl.saveLink(linkDto,groupIds,authentication.getName(),linkImgFile);
        linkGroupServiceImpl.changeGroupImgArray(groupIds);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedLink);
    }

    @PatchMapping ("/link/{linkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "링크 수정 API")
    public ResponseEntity<?> updateLink(@PathVariable("linkId") Long linkId,
                                            @RequestPart @Valid LinkDto linkDto,
                                            @RequestPart(value = "groupId") Long[] groupIds,
                                            @RequestPart(value = "linkImgFile", required = false) MultipartFile linkImgFile) {
        LinkInfoDto updatedLink = linkServiceImpl.updateLink(linkId, linkDto, groupIds, linkImgFile);
        return ResponseEntity.ok().body(updatedLink);
    }

    @DeleteMapping("/link/{linkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "링크 삭제 API")
    public ResponseEntity<?> deleteLink(@PathVariable("linkId") Long linkId){
        linkServiceImpl.deleteLink(linkId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

