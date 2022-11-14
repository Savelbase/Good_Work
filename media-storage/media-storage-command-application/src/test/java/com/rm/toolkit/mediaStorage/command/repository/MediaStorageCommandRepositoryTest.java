package com.rm.toolkit.mediaStorage.command.repository;

import com.rm.toolkit.mediaStorage.command.model.MediaFileData;
import com.rm.toolkit.mediaStorage.command.repository.MediaStorageRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//@TestPropertySource("classpath:application.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MediaStorageCommandRepositoryTest {
    @Autowired
    public MediaStorageRepository mediaStorageRepository;
    private final static String USER_ID = "One";

    private final Supplier<MediaFileData> mediaFileDataSupplier = () -> {
        MediaFileData data = new MediaFileData();
        data.setId("Abba");
        data.setUrl("1");
        data.setConfirmed(false);
        data.setUserId(USER_ID);
        data.setUploadDate(new Date(9L));
        return data;
    };

    @BeforeEach
    void truncateTable() {
        mediaStorageRepository.deleteAll();
    }

    @Test
    void shouldFindDataByUserId() {
        MediaFileData data = mediaFileDataSupplier.get();

        mediaStorageRepository.save(data);

        assertNotNull(mediaStorageRepository.findFirstByUserId(USER_ID));
    }

    @Test
    void shouldFindExpiredFiles() {
        MediaFileData data = mediaFileDataSupplier.get();

        mediaStorageRepository.save(data);

        int daysToAdd = 3;
        assertNotNull(mediaStorageRepository.findAllByExpiryDateLessThan(Date.valueOf(LocalDate.now().plusDays(daysToAdd))));
    }

    @Test
    void shouldNotFindConfirmedFiles() {
        MediaFileData data = mediaFileDataSupplier.get();
        data.setConfirmed(true);

        mediaStorageRepository.save(data);

        int daysToAdd = 3;
        assertNotNull(mediaStorageRepository.findAllByExpiryDateLessThan(Date.valueOf(LocalDate.now().plusDays(daysToAdd))));
    }
}
