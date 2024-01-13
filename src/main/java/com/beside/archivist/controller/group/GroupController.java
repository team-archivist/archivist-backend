package com.beside.archivist.controller.group;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.dto.link.LinkDto;
import com.beside.archivist.entity.usergroup.UserGroup;
import com.beside.archivist.service.usergroup.UserGroupService;
import com.beside.archivist.service.group.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupServiceImpl;
    private final UserGroupService userGroupServiceImpl;

    /** 특정 유저가 생성한 모든 그룹 조회 **/
    @GetMapping("/user/group/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<List<GroupDto>> getUserGroupList(@PathVariable("userId") Long userId) {
        List<UserGroup> userGroups = userGroupServiceImpl.getUserGroupsByUserId(userId);
        List<GroupDto> groups = groupServiceImpl.getGroupsByUserGroup(userGroups);
        return ResponseEntity.ok().body(groups);
    }

    /** 특정 그룹 상세 조회 **/
    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> findGroupById(@PathVariable("groupId") Long id) {
        GroupDto group = groupServiceImpl.findGroupById(id);
        return ResponseEntity.ok().body(group);
    }

    /** 그룹 생성 **/
    @PostMapping("/group")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerGroup(@RequestPart @Valid GroupDto groupDto,
                                              @RequestPart(value = "groupImgFile", required = false) MultipartFile groupImgFile) {
        GroupDto savedGroup = groupServiceImpl.saveGroup(groupDto,groupImgFile);
        userGroupServiceImpl.saveUserGroup(savedGroup.getGroupId());
        return ResponseEntity.ok().body(savedGroup);
    }

    /** 그룹 수정 **/
    @PatchMapping ("/group/{groupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateGroup(@PathVariable("groupId") Long groupId,
                                            @RequestPart @Valid GroupDto groupDto,
                                            @RequestPart(value = "groupImgFile", required = false) MultipartFile groupImgFile) {
        GroupDto updatedGroup = groupServiceImpl.updateGroup(groupId, groupDto, groupImgFile);
        return ResponseEntity.ok().body(updatedGroup);
    }

    /** 그룹 삭제 **/
    @DeleteMapping("/group/{groupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteGroup(@PathVariable("groupId") Long groupId){
        groupServiceImpl.deleteGroup(groupId);
        return ResponseEntity.ok().body("그룹 삭제 완료.");
    }

    /** 특정 그룹에 속한 링크들 모두 조회 **/
    @GetMapping("/group/link/{groupId}")
    public ResponseEntity<?> getLinksByGroupId(@PathVariable("groupId") Long id) {
        List<LinkDto> group = groupServiceImpl.getLinksByGroupId(id);
        return ResponseEntity.ok().body(group);
    }
}

