package com.beside.archivist.repository.link;

import com.beside.archivist.entity.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link,Long> {

    List<Link> findByUsers_Id(Long userId);
}
