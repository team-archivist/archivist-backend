package com.beside.archivist.repository.link;

import com.beside.archivist.entity.link.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface LinkRepository extends JpaRepository<Link,Long> {

}
