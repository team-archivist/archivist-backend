package com.beside.archivist.controller.group;


import com.beside.archivist.dto.group.LinkGroupDto;
import com.beside.archivist.service.group.LinkGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class LinkGroupController {

    private final LinkGroupService linkGroupServiceImpl;

    /** 그룹에 링크 저장 **/
    @PostMapping("/group/link")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerLinkGroup(@RequestPart @Valid LinkGroupDto linkGroupDto) {
        LinkGroupDto savedLinkGroup = linkGroupServiceImpl.saveLinkGroup(linkGroupDto);
        linkGroupServiceImpl.changeGroupImg(linkGroupDto.getGroupId()); // 그룹 이미지 변경
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLinkGroup);
    }
    
    /** 그룹에서 링크 제거하기 **/
    @DeleteMapping("/group/link/{linkGroupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteLinkGroup(@PathVariable("linkGroupId") Long linkGroupId){
        linkGroupServiceImpl.deleteLinkGroup(linkGroupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}