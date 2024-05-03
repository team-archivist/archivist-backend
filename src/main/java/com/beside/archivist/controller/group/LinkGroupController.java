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

    @PostMapping("/group/link")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "그룹 내 링크 저장 API")
    public ResponseEntity<?> registerLinkGroup(@RequestPart @Valid LinkGroupDto linkGroupDto) {
        LinkGroupDto savedLinkGroup = linkGroupServiceImpl.saveLinkGroup(linkGroupDto);
        linkGroupServiceImpl.changeGroupImg(linkGroupDto.getGroupId()); // 그룹 이미지 변경
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLinkGroup);
    }
    
    @DeleteMapping("/group/link/{linkGroupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "그룹 내 링크 제거 API")
    public ResponseEntity<?> deleteLinkGroup(@PathVariable("linkGroupId") Long linkGroupId){
        linkGroupServiceImpl.deleteLinkGroup(linkGroupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}