package com.rm.toolkit.mediaStorage.query.repository;

import com.rm.toolkit.mediaStorage.query.model.MediaFileData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

//@TestPropertySource("classpath:application.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MediaStorageRepositoryTest {
    @Autowired
    public MediaStorageRepository mediaStorageRepository;

    private final Supplier<MediaFileData> mediaFileDataSupplier = () -> {
        MediaFileData data = new MediaFileData();
        data.setId("Id");
        data.setUrl("1");
        data.setConfirmed(false);
        data.setUploadDate(new Date(9L));
        return data;
    };

    @BeforeEach
    void truncateTable() {
        mediaStorageRepository.deleteAll();
    }

    @Test
    void shouldFindFirstById() {
        String id = "Id";
        MediaFileData data = mediaFileDataSupplier.get();

        mediaStorageRepository.save(data);

        assertNotNull(mediaStorageRepository.findFirstById(id));
    }

    @Test
    void shouldFindFirstByUrl() {
        String url = "Url";
        MediaFileData data = mediaFileDataSupplier.get();
        data.setUrl(url);

        mediaStorageRepository.save(data);

        assertNotNull(mediaStorageRepository.findFileByUrl(url));
    }
}