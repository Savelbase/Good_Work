package com.rm.toolkit.mediaStorage.query.service;

import com.rm.toolkit.mediaStorage.query.util.MediaStorageQueryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MediaStorageQueryService {

    private final MediaStorageQueryUtil mediaStorageQueryUtil;

    public byte[] loadFileById(String id) {
        return mediaStorageQueryUtil.getFile(id);
    }
}
