package com.beside.archivist.repository.group;


import com.beside.archivist.entity.group.LinkGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkGroupRepository extends JpaRepository<LinkGroup,Long> {
    List<LinkGroup> findByGroup_Id(Long groupId);
}