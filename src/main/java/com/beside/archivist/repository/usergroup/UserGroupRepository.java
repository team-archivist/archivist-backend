package com.beside.archivist.repository.usergroup;

import com.beside.archivist.entity.usergroup.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
