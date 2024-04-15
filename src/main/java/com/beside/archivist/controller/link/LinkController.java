package com.beside.archivist.controller.link;

import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.dto.link.LinkInfoDto;
import com.beside.archivist.entity.group.Group;
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

    /** 회원이 저장한 링크 모두 조회 **/
    @GetMapping("/user/link/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<List<LinkDto>> getUserLinkList(@PathVariable("userId") Long userId) {
        List<LinkDto> links = linkServiceImpl.getLinksByUserId(userId);
        return ResponseEntity.ok().body(links);
    }

    /** 특정 링크 상세 조회 **/
    @GetMapping("/link/{linkId}")
    public ResponseEntity<?> findLinkById(@PathVariable("linkId") Long id) {
        LinkDto link = linkServiceImpl.findLinkById(id);
        List<Group> groupList = linkServiceImpl.getGroupsByLinkId(id);

        LinkInfoDto linkInfoDto = LinkInfoDto.builder()
                .linkId(link.getLinkId())
                .linkUrl(link.getLinkUrl())
                .linkName(link.getLinkName())
                .linkDesc(link.getLinkDesc())
                .imgUrl(link.getLinkUrl())
                .groupList(groupList)
                .build();

        return ResponseEntity.ok().body(linkInfoDto);
    }

    /** URL 유효성 검증 **/
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

    /** 외부 웹에서 링크 저장 **/
    @PostMapping("/link")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerLink(@RequestPart @Valid LinkDto linkDto,
                                          Authentication authentication,
                                          @RequestPart(value = "groupId") Long[] groupIds,
                                          @RequestPart(value = "linkImgFile", required = false) MultipartFile linkImgFile) {
        if (authentication == null){
            throw new MissingAuthenticationException(ExceptionCode.MISSING_AUTHENTICATION);
        }

        LinkDto savedLink = linkServiceImpl.saveLink(linkDto,groupIds,authentication.getName(),linkImgFile);
        linkGroupServiceImpl.changeGroupImgArray(groupIds);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedLink);
    }

    /** 링크 수정 **/
    @PatchMapping ("/link/{linkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateLink(@PathVariable("linkId") Long linkId,
                                            @RequestPart @Valid LinkDto linkDto,
                                            @RequestPart(value = "groupId") Long[] groupIds,
                                            @RequestPart(value = "linkImgFile", required = false) MultipartFile linkImgFile) {
        LinkDto updatedLink = linkServiceImpl.updateLink(linkId, linkDto, groupIds, linkImgFile);
        return ResponseEntity.ok().body(updatedLink);
    }

    /** 링크 삭제 **/
    @DeleteMapping("/link/{linkId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteLink(@PathVariable("linkId") Long linkId){
        linkServiceImpl.deleteLink(linkId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

