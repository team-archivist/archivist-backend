package com.beside.archivist.controller.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.group.GroupInfoDto;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.dto.link.LinkInfoDto;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.users.MissingAuthenticationException;
import com.beside.archivist.service.usergroup.UserGroupService;
import com.beside.archivist.service.group.GroupService;
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

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupServiceImpl;
    private final UserGroupService userGroupServiceImpl;

    @GetMapping("/user/group/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "해당 유저의 모든 그룹 조회 API")
    public ResponseEntity<List<GroupDto>> getUserGroupList(@PathVariable("userId") Long userId) {
        List<GroupDto> groups = userGroupServiceImpl.getGroupDtoByUserId(userId, true);
        return ResponseEntity.ok().body(groups);
    }

    @GetMapping("/group/{groupId}")
    @Operation(summary = "특정 그룹 상세 조회 API")
    public ResponseEntity<?> findGroupById(@PathVariable("groupId") Long id) {
        GroupInfoDto group = groupServiceImpl.findGroupById(id);
        return ResponseEntity.ok().body(group);
    }

    @PostMapping("/group")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "그룹 생성 API")
    public ResponseEntity<?> registerGroup(@RequestPart @Valid GroupDto groupDto, Authentication authentication,
                                              @RequestPart(value = "groupImgFile", required = false) MultipartFile groupImgFile) {
        if (authentication == null){
            throw new MissingAuthenticationException(ExceptionCode.MISSING_AUTHENTICATION);
        }

        GroupInfoDto savedGroup = groupServiceImpl.saveGroup(groupDto,groupImgFile);
        userGroupServiceImpl.saveUserGroup(savedGroup.getGroupId(),authentication.getName(),true);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }

    @PatchMapping ("/group/{groupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "그룹 수정 API")
    public ResponseEntity<?> updateGroup(@PathVariable("groupId") Long groupId,
                                            @RequestPart @Valid GroupDto groupDto,
                                            @RequestPart(value = "groupImgFile", required = false) MultipartFile groupImgFile) {
        GroupInfoDto updatedGroup = groupServiceImpl.updateGroup(groupId, groupDto, groupImgFile);
        return ResponseEntity.ok().body(updatedGroup);
    }

    @DeleteMapping("/group/{groupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") }, summary = "그룹 삭제 API")
    public ResponseEntity<?> deleteGroup(@PathVariable("groupId") Long groupId){
        groupServiceImpl.deleteGroup(groupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/group/link/{groupId}")
    @Operation(summary = "특정 그룹에 속한 링크들 모두 조회 API")
    public ResponseEntity<?> getLinksByGroupId(@PathVariable("groupId") Long id) {
        List<LinkInfoDto> group = groupServiceImpl.getLinksByGroupId(id);
        return ResponseEntity.ok().body(group);
    }
}

