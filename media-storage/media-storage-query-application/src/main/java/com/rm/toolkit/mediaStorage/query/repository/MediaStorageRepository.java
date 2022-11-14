package com.rm.toolkit.mediaStorage.query.repository;

import com.rm.toolkit.mediaStorage.query.model.MediaFileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MediaStorageRepository extends JpaRepository<MediaFileData, String> {

    @Query("SELECT a FROM MediaFileData a WHERE a.url=:url")
    Optional<MediaFileData> findFileByUrl(@Param("url") String url);

    Optional<MediaFileData> findFirstById(@Param("id") String id);
}
