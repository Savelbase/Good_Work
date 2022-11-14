package com.rm.toolkit.mediaStorage.command.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Component
public class MediaFileOperation {

    private final MediaStorageCommandUtil mediaStorageCommandUtil;

    //раз в сутки в 0:00
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteExpiredFiles() {
        mediaStorageCommandUtil.deleteFiles();
        log.info("Неиспользуемые файлы удалены из бд");
    }
}
