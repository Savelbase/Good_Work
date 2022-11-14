package com.rm.toolkit.feedbackcommandapplication.util;

import com.rm.toolkit.feedbackcommandapplication.model.FeedbackNotification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FeedbackNotificationUtil {

    private static final Integer VERSION_VALUE = 1;
    private static final Integer MIN_NOTIFICATION_COUNTER = 0;

    public FeedbackNotification createNotificationWithoutDateTime(String completedOneToOneId) {
        return FeedbackNotification.builder()
                .id(UUID.randomUUID().toString())
                .oneToOneId(completedOneToOneId)
                .notificationCounter(MIN_NOTIFICATION_COUNTER)
                .rdIsNotified(false)
                .version(VERSION_VALUE)
                .build();
    }
}
