package com.beside.archivist.repository.group;

import com.beside.archivist.entity.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.links WHERE g.id = :groupId")
    Optional<Group> findByIdWithLinks(@Param("groupId") Long groupId);
}
