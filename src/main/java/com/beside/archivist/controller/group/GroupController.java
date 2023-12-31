package com.beside.archivist.controller.group;

import com.beside.archivist.dto.group.GroupDto;
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

    @GetMapping("/user/group/{userId}")
    public ResponseEntity<List<GroupDto>> getUserGroupList(@PathVariable("userId") Long userId) {
        List<GroupDto> groups = groupServiceImpl.getGroupsByUserId(userId);
        return ResponseEntity.ok().body(groups);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<?> findGroupById(@PathVariable("id") Long id) {
        GroupDto group = groupServiceImpl.findGroupById(id);
        return ResponseEntity.ok().body(group);
    }

    @PostMapping("/group")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> registerGroup(@RequestPart @Valid GroupDto groupDto,
                                              @RequestPart(value = "groupImgFile", required = false) MultipartFile groupImgFile) {
        GroupDto savedGroup = groupServiceImpl.saveGroup(groupDto,groupImgFile);
        return ResponseEntity.ok().body(savedGroup);
    }

    @PatchMapping ("/group/{groupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> updateGroup(@PathVariable("groupId") Long groupId,
                                            @RequestPart @Valid GroupDto groupDto,
                                            @RequestPart(value = "groupImgFile", required = false) MultipartFile groupImgFile) {
        GroupDto updatedGroup = groupServiceImpl.updateGroup(groupId, groupDto, groupImgFile);
        return ResponseEntity.ok().body(updatedGroup);
    }


    @DeleteMapping("/group/{groupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteGroup(@PathVariable("groupId") Long groupId){
        groupServiceImpl.deleteGroup(groupId);
        return ResponseEntity.ok().body("그룹 삭제 완료.");
    }
}

