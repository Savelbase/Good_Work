package com.rm.toolkit.oneToOne.command.util;

import com.netflix.servo.util.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UuidProvider {
    String uuid();

    @VisibleForTesting
    class Fake implements UuidProvider {
        @Override
        public String uuid() {
            return UUID.fromString("0000-00-00-00-000000").toString();
        }
    }
}
