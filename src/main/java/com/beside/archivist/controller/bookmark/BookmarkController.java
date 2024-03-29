package com.beside.archivist.controller.bookmark;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.service.group.GroupService;
import com.beside.archivist.service.usergroup.UserGroupService;
import com.beside.archivist.service.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
public class BookmarkController {
    private final UserGroupService userGroupServiceImpl;
    private final GroupService groupServiceImpl;
    private final UserService userServiceImpl;

    /** 다른 회원의 그룹 북마크 하기 **/
    @PostMapping("/bookmark/{groupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> bookmarkGroup(@PathVariable("groupId") Long groupId) {
        userGroupServiceImpl.saveUserGroup(groupId,false);
        return ResponseEntity.status(HttpStatus.CREATED).body("북마크가 완료되었습니다.");
    }

    /** 내가 북마크한 그룹 모두 조회하기 **/
    @GetMapping("/user/bookmark/{userId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> getBookmarks(@PathVariable("userId") Long userId) {
        List<GroupDto> groups = userGroupServiceImpl.getGroupDtoByUserId(userId, false);
        return ResponseEntity.ok().body(groups);
    }

    /** 내가 북마크한 그룹에서 제거하기 **/
    @DeleteMapping("/bookmark/{groupId}")
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    public ResponseEntity<?> deleteBookmark(@PathVariable("groupId") Long groupId, Authentication authentication) {
        Long userId = userServiceImpl.getUserByEmail(authentication.getName()).getId();
        userGroupServiceImpl.deleteBookmark(userId, groupId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
