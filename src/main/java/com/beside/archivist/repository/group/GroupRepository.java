package com.beside.archivist.repository.group;

import com.beside.archivist.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

    List<Group> findByUsers_Id(Long userId);
}
