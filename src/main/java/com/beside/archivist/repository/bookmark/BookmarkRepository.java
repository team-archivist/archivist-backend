package com.beside.archivist.repository.bookmark;

import com.beside.archivist.entity.bookmark.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    List<Bookmark> findByUsers_Id(Long userId);
}
