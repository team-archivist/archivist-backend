package com.beside.archivist.repository.usergroup;

import com.beside.archivist.entity.usergroup.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByUsers_Id(Long userId);
    void deleteByUsers_IdAndGroups_Id(Long userId, Long groupId);
}
