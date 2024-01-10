package com.beside.archivist.service.usergroup;

import com.beside.archivist.config.AuditConfig;
import com.beside.archivist.entity.group.Group;
import com.beside.archivist.entity.usergroup.UserGroup;
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
    public void saveUserGroup(Long groupId) {
        String userEmail = auditConfig.auditorProvider().getCurrentAuditor()
                .orElseThrow(RuntimeException::new); // todo: 인증 X, 예외 처리

        UserGroup userGroup = UserGroup.builder()
                .isOwner(true) // 내가 생성한 그룹
                .users(userServiceImpl.getUserByEmail(userEmail))
                .groups(groupServiceImpl.getGroup(groupId))
                .build();

        userGroup.getUsers().addUserGroup(userGroup);
        userGroup.getGroups().addUserGroup(userGroup);
        userGroupRepository.save(userGroup);
    }

    @Override
    public List<UserGroup> getUserGroupsByUserId(Long userId) {
        return userGroupRepository.findByUsers_Id(userId).stream()
                .filter(UserGroup::isOwner) // 본인이 생성한 그룹만 조회
                .toList();
    }
}
