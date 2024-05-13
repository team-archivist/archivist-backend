package com.beside.archivist.repository.home;

import com.beside.archivist.entity.home.HomeFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeFeedRepository extends JpaRepository<HomeFeed,Long> {
    List<HomeFeed> findByType(HomeFeed.Type type);

}
