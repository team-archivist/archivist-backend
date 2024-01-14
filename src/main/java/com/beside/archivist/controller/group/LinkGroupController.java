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
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerLinkGroup(@RequestPart @Valid LinkGroupDto linkGroupDto) {
        LinkGroupDto savedLinkGroup = linkGroupServiceImpl.saveLinkGroup(linkGroupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLinkGroup);
    }
    

    @DeleteMapping("/group/link/{linkGroupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteLinkGroup(@PathVariable("linkGroupId") Long linkGroupId){
        linkGroupServiceImpl.deleteLinkGroup(linkGroupId);
        return ResponseEntity.ok().body("그룹 링크 삭제 완료.");
    }
}