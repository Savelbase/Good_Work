package com.rm.toolkit.mediaStorage.command.repository;

import com.rm.toolkit.mediaStorage.command.model.MediaFileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface MediaStorageRepository extends JpaRepository<MediaFileData, String> {

    @Modifying
    @Query("SELECT u FROM MediaFileData u WHERE u.uploadDate < :date AND u.isConfirmed = false")
    List<MediaFileData> findAllByExpiryDateLessThan(@Param("date") Date date);

    MediaFileData findFirstByUserId(String userId);
}
