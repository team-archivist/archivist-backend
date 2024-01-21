package com.beside.archivist.service.usergroup;

import com.beside.archivist.config.AuditConfig;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.usergroup.UserGroup;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.group.GroupAlreadyExistsException;
import com.beside.archivist.exception.link.GroupInBookmarkNotFoundException;
import com.beside.archivist.exception.users.MissingAuthenticationException;
import com.beside.archivist.repository.usergroup.UserGroupRepository;
import com.beside.archivist.service.group.GroupService;
import com.beside.archivist.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final UserService userServiceImpl;
    private final GroupService groupServiceImpl;
    private final AuditConfig auditConfig;
    @Override
    public void saveUserGroup(Long groupId, boolean isOwner) {
        String userEmail = auditConfig.auditorProvider().getCurrentAuditor()
                .orElseThrow(()-> new MissingAuthenticationException(ExceptionCode.MISSING_AUTHENTICATION));
        User findUser = userServiceImpl.getUserByEmail(userEmail);
        Group findGroup = groupServiceImpl.getGroup(groupId);

        checkDuplicateGroup(findUser.getId(), findGroup.getId(), isOwner);
        
        // todo: 내 그룹을 북마크 하는 경우 예외 처리
        UserGroup userGroup = UserGroup.builder()
                .isOwner(isOwner) // save / bookmark 그룹 구분
                .users(userServiceImpl.getUserByEmail(userEmail))
                .groups(groupServiceImpl.getGroup(groupId))
                .build();

        userGroup.getUsers().addUserGroup(userGroup);
        userGroup.getGroups().addUserGroup(userGroup);
        userGroupRepository.save(userGroup);
    }

    @Override
    public void checkDuplicateGroup(Long userId, Long groupId, boolean isOwner) {
        userGroupRepository.findByUsers_IdAndGroups_Id(userId, groupId).ifPresent(userGroup -> {
            if (userGroup.isOwner() || !isOwner) { // 이미 내 그룹으로 저장되어 있거나 북마크 그룹으로 저장하였을 때
                throw new GroupAlreadyExistsException(ExceptionCode.GROUP_ALREADY_EXISTS);
            }
        });
    }

    @Override
    public List<UserGroup> getUserGroupsByUserId(Long userId, boolean isOwner) {
        return userGroupRepository.findByUsers_Id(userId).stream()
                .filter(userGroup -> userGroup.isOwner() == isOwner)// 본인이 생성한 그룹만 조회
                .toList();
    }

    @Override
    public void deleteBookmark(Long userId, Long groupId) {
        UserGroup userGroup = userGroupRepository.findByUsers_IdAndGroups_Id(userId, groupId)
                .orElseThrow(() -> new GroupInBookmarkNotFoundException(ExceptionCode.GROUP_IN_BOOKMARK_NOT_FOUND));
        if(userGroup.isOwner()){
            throw new GroupInBookmarkNotFoundException(ExceptionCode.GROUP_IN_BOOKMARK_NOT_FOUND);
        }
        userGroupRepository.delete(userGroup);
    }
}
