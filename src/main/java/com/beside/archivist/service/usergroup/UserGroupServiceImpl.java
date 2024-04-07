package com.beside.archivist.service.usergroup;

import com.beside.archivist.dto.group.GroupDto;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.usergroup.UserGroup;
import com.beside.archivist.entity.users.User;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.group.GroupAlreadyExistsException;
import com.beside.archivist.exception.group.GroupInBookmarkNotFoundException;
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
    @Override
    public void saveUserGroup(Long groupId,String email, boolean isOwner) {
        User findUser = userServiceImpl.getUserByEmail(email);
        Group findGroup = groupServiceImpl.getGroup(groupId);

        checkDuplicateGroup(findUser.getId(), findGroup.getId(), isOwner);
        
        UserGroup userGroup = UserGroup.builder()
                .isOwner(isOwner) // save / bookmark 그룹 구분
                .users(userServiceImpl.getUserByEmail(email))
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
    public List<GroupDto> getGroupDtoByUserId(Long userId, boolean isOwner) {
        return userGroupRepository.getGroupsByUserId(userId,isOwner);
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
